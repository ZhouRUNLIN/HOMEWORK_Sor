const express = require('express');
const cors = require('cors');
const ethers = require('ethers');
const CollectionABI = require('../contracts/artifacts/src/Collection.sol/Collection.json').abi;
const MainABI = require('../contracts/artifacts/src/Main.sol/Main.json').abi;
const BoostersABI = require('../contracts/artifacts/src/Boosters.sol/Boosters.json').abi;
const { PokemonTCG } = require('pokemon-tcg-sdk-typescript');

const app = express();
const port = 3000;

// 设置 provider 和合约实例
const provider = new ethers.JsonRpcProvider('http://127.0.0.1:8545'); // 使用正确的 provider
const mainContractAddress = "0x5FbDB2315678afecb367f032d93F642f64180aa3"; // 替换为实际的主合约地址
const mainContract = new ethers.Contract(mainContractAddress, MainABI, provider);

// 解析 JSON 请求体
app.use(express.json());

// 缓存数据
let userCardsCache = {};
let userBoostersCache = {};
let forSaleBoostersCache = [];

const zeroAddress = '0x0000000000000000000000000000000000000000';

// 工具函数：递归地将对象中的 BigInt 转换为字符串
const convertBigIntToString = (obj) => {
  if (typeof obj === 'bigint') {
    return obj.toString();  // 将 BigInt 转为字符串
  } else if (Array.isArray(obj)) {
    return obj.map(convertBigIntToString);  // 递归处理数组
  } else if (typeof obj === 'object' && obj !== null) {
    return Object.fromEntries(Object.entries(obj).map(([key, value]) => [key, convertBigIntToString(value)]));
  }
  return obj;  // 其他类型直接返回
};

let pokemonCardsCache = []; // 初始化为一个空数组

const fetchPokemonCards = async () => {
  try {
    console.log("Fetching Pokémon cards...");
    const response = await PokemonTCG.findSetsByQueries({ q: 'id:base1' });
    if (response.length === 0) {
      throw new Error("Base set not found");
    }
    const baseSetId = response[0].id;
    const cardsResponse = await PokemonTCG.findCardsByQueries({ q: `set.id:${baseSetId}` });
    pokemonCardsCache = cardsResponse; // 获取前100张卡片
    console.log("Pokémon cards fetched successfully!");
  } catch (error) {
    console.error("Error fetching Pokémon cards from API:", error);
  }
};

// 从链上获取所有用户的卡牌数据
const fetchUserCards = async () => {
  try {
    // 确保 pokemonCardsCache 已经初始化
    if (pokemonCardsCache.length === 0) {
      console.error("Error: pokemonCardsCache is empty. Make sure to fetch Pokemon cards first.");
      return;
    }

    console.log("Fetching total collections...");
    const totalCollections = await mainContract.totalCollections();
    let totalCollectionsNumber = Number(totalCollections);

    console.log("Total collections:", totalCollectionsNumber);

    const userCards = {};

    for (let i = 0; i < totalCollectionsNumber; i++) {
      console.log(`Fetching collection address for collection ${i}`);
      const collectionAddress = await mainContract.getCollection(i);
      console.log(`Collection address ${i}:`, collectionAddress);

      const collection = new ethers.Contract(collectionAddress, CollectionABI, provider);

      console.log("Querying Transfer events...");
      const events = await collection.queryFilter(collection.filters.Transfer());
      console.log(`Found ${events.length} Transfer events`);

      for (const event of events) {
        const { from, to, tokenId } = event.args;
        console.log(`Event: from ${from}, to ${to}, tokenId ${tokenId.toString()}`);

        const userAddress = to.toLowerCase(); // 统一转换为小写，便于处理

        if (!userCards[userAddress]) {
          userCards[userAddress] = [];
        }

        const cardIndex = (Number(tokenId) - 1); // tokenId 从 1 开始，与数组索引对应
        const cardDetails = pokemonCardsCache[cardIndex]; // 确保索引不超出范围

        if (cardDetails) {
          // 从合约中获取卡牌的详细信息
          const cardDetailFromContract = await collection.cardDetails(tokenId);

          userCards[userAddress].push({
            tokenId: tokenId.toString(),  // 将 BigInt 转换为字符串
            name: cardDetails.name,  // 使用 Pokémon 卡牌名称
            image: cardDetails.images.large,  // 使用 Pokémon 卡牌图片
            isForSale: cardDetailFromContract.isForSale,
            price: cardDetailFromContract.price.toString(),
            collectionId: i.toString(),
            collectionAddress: collectionAddress
          });
        } else {
          console.error(`Card details for tokenId ${tokenId} not found in pokemonCardsCache.`);
        }
      }
    }

    userCardsCache = userCards;
    console.log("User cards data cached successfully!");

  } catch (error) {
    console.error("Error fetching user cards from blockchain:", error);
  }
};

// 从链上获取所有用户的 Booster 数据
const fetchBoostersData = async () => {
  try {
    console.log("Fetching total boosters...");
    const totalBoosters = await mainContract.totalBoosters();
    let totalBoostersNumber = Number(totalBoosters);

    console.log(`Total boosters: ${totalBoostersNumber}`);

    const userBoosters = {};
    const forSaleBoosters = [];

    // 处理 Boosters
    for (let i = 0; i < totalBoostersNumber; i++) {
      console.log(`Fetching booster address for booster ${i}`);
      const boosterAddress = await mainContract.getBoosters(i); // 确保调用的是 `getBoosters`
      console.log(`Booster address ${i}: ${boosterAddress}`);

      const booster = new ethers.Contract(boosterAddress, BoostersABI, provider);

      console.log("Querying Transfer events for Booster...");
      const events = await booster.queryFilter(booster.filters.Transfer());
      console.log(`Found ${events.length} Transfer events for Booster ${i}`);

      for (const event of events) {
        const { from, to, tokenId } = event.args;
        console.log(`Booster Event: from ${from}, to ${to}, tokenId ${tokenId.toString()}`);

        const fromAddress = from.toLowerCase();
        const toAddress = to.toLowerCase();

        // 从前任持有者的 Booster 列表中移除 Booster（如果不是铸造事件）
        if (fromAddress !== zeroAddress) {
          if (userBoosters[fromAddress]) {
            // 移除 Booster
            userBoosters[fromAddress] = userBoosters[fromAddress].filter(
                (boosterItem) => !(boosterItem.tokenId === tokenId.toString() && boosterItem.boosterAddress === boosterAddress)
            );
          }
        }

        // 添加 Booster 到新持有者的列表
        if (!userBoosters[toAddress]) {
          userBoosters[toAddress] = [];
        }

        // 从合约中获取 Booster 的详细信息
        const boosterDetails = await booster.itemDetails(tokenId);
        const boosterImgURIs = await booster.imgURIs(tokenId);

        userBoosters[toAddress].push({
          tokenId: tokenId.toString(),
          name: `Booster #${tokenId.toString()}`,
          image: boosterImgURIs,
          isForSale: boosterDetails.isForSale,
          price: boosterDetails.price.toString(),
          boosterId: i.toString(),
          boosterAddress: boosterAddress
        });

        // 如果 Booster 在售，添加到 forSaleBoostersCache
        if (boosterDetails.isForSale) {
          forSaleBoosters.push({
            tokenId: tokenId.toString(),
            name: `Booster #${tokenId.toString()}`,
            image: boosterImgURIs,
            price: boosterDetails.price.toString(),
            boosterId: i.toString(),
            boosterAddress: boosterAddress,
            owner: toAddress
          });
        }
      }
    }

    userBoostersCache = userBoosters;
    forSaleBoostersCache = forSaleBoosters;

    console.log("User boosters data cached successfully!");
  } catch (error) {
    console.error("Error fetching boosters data from blockchain:", error);
  }
};

// 初始化时从链上加载数据
(async () => {
  try {
    await fetchPokemonCards(); // 等待 Pokémon 卡牌数据加载完毕
    await fetchUserCards(); // 等待用户卡牌数据加载完毕
    await fetchBoostersData();
  } catch (error) {
    console.error("Error initializing card data:", error);
  }
})();

// 定期更新缓存（可选）
setInterval(fetchUserCards, 5000); // 每5s更新一次
setInterval(fetchBoostersData, 5000);

// CORS 设置
app.use(cors());

// API 端点 - 获取所有用户的卡牌数据
app.get('/api/cards', (req, res) => {
  const userId = req.query.userId;
  const cardId = req.query.cardId;

  if (userId) {
    const userCards = userCardsCache[userId.toLowerCase()]; // 使用小写进行匹配
    if (userCards) {
      res.json(convertBigIntToString(userCards));  // 返回该用户的卡牌，确保 BigInt 被转换为字符串
    } else {
      res.status(404).json({ error: 'User not found' });
    }
  } else if (cardId) {
    const cardIdNumber = cardId.toString(); // 将 cardId 转换为字符串进行比较
    const cards = Object.values(userCardsCache).flat();
    const card = cards.find(c => c.tokenId === cardIdNumber);
    if (card) {
      res.json(convertBigIntToString(card));  // 确保卡牌数据中的 BigInt 被转换为字符串
    } else {
      res.status(404).json({ error: 'Card not found' });
    }
  } else {
    res.json(convertBigIntToString(userCardsCache)); // 当未传递 userId 或 cardId 时返回所有用户的卡牌，确保 BigInt 转换为字符串
  }
});

// API 端点 - 获取所有待出售的卡牌
app.get('/api/cards/for-sale', (req, res) => {
  try {
    const forSaleCards = [];

    Object.values(userCardsCache).forEach(userCards => {
      userCards.forEach(card => {
        if (card.isForSale) {
          forSaleCards.push(card);
        }
      });
    });

    res.json(convertBigIntToString(forSaleCards)); // 返回所有待出售的卡牌
  } catch (error) {
    console.error("Error fetching for-sale cards:", error);
    res.status(500).json({ error: 'An error occurred while fetching for-sale cards.' });
  }
});

// API 端点 - 获取链上所有用户的 ID 集合
app.get('/nft/all', (req, res) => {
  try {
    const userIds = Object.keys(userCardsCache);
    res.json(userIds);
  } catch (error) {
    console.error("Error fetching all user IDs:", error);
    res.status(500).json({ error: 'An error occurred while fetching all user IDs.' });
  }
});

// API 端点 - 获取特定用户的卡片集合
app.get('/nft/userID', (req, res) => {
  const userId = req.query.userId;

  if (userId) {
    const userCards = userCardsCache[userId.toLowerCase()]; // 使用小写进行匹配
    if (userCards) {
      res.json(convertBigIntToString(userCards));  // 返回该用户的卡牌集合，确保 BigInt 被转换为字符串
    } else {
      res.status(404).json({ error: 'User not found' });
    }
  } else {
    res.status(400).json({ error: 'User ID is required' });
  }
});

// API 端点 - 获取所有用户的 Boosters 数据
app.get('/api/boosters', (req, res) => {
  const userId = req.query.userId;
  const boosterId = req.query.boosterId;

  if (userId) {
    const userBoosters = userBoostersCache[userId.toLowerCase()]; // 使用小写进行匹配
    if (userBoosters) {
      res.json(convertBigIntToString(userBoosters));  // 返回该用户的 Boosters，确保 BigInt 被转换为字符串
    } else {
      res.status(404).json({ error: 'User not found' });
    }
  } else if (boosterId) {
    const boosterIdNumber = boosterId.toString(); // 将 boosterId 转换为字符串进行比较
    const boosters = Object.values(userBoostersCache).flat();
    const booster = boosters.find(b => b.tokenId === boosterIdNumber);
    if (booster) {
      res.json(convertBigIntToString(booster));  // 确保 Booster 数据中的 BigInt 被转换为字符串
    } else {
      res.status(404).json({ error: 'Booster not found' });
    }
  } else {
    res.json(convertBigIntToString(userBoostersCache)); // 当未传递 userId 或 boosterId 时返回所有用户的 Boosters，确保 BigInt 转换为字符串
  }
});

// API 端点 - 获取所有待出售的 Boosters
app.get('/api/boosters/for-sale', (req, res) => {
  try {
    res.json(convertBigIntToString(forSaleBoostersCache)); // 返回所有待出售的 Boosters
  } catch (error) {
    console.error("Error fetching for-sale boosters:", error);
    res.status(500).json({ error: 'An error occurred while fetching for-sale boosters.' });
  }
});

// 启动服务器
app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});