import { HardhatRuntimeEnvironment } from "hardhat/types";
import { DeployFunction } from "hardhat-deploy/types";
import { ethers } from "hardhat";
import { PokemonTCG } from "pokemon-tcg-sdk-typescript";
import dotenv from "dotenv";

dotenv.config();

const setupCollectionsAndBoosters: DeployFunction = async function (hre: HardhatRuntimeEnvironment) {
    const { deployments, getNamedAccounts } = hre;
    const { deployer } = await getNamedAccounts();
    let tokenId = 0;

    const main = await ethers.getContract("Main", deployer);

    const accounts = await ethers.getSigners();
    const userAddresses = accounts.map(account => account.address);

    const ownerAddress = await main.owner();
    console.log(`Current contract owner: ${ownerAddress}`);

    if (ownerAddress !== deployer) {
        throw new Error("The deployer is not the owner of the contract. Please verify the deployer address.");
    }


    console.log("Creating new collection...");
    const collectionTx = await main.createCollection("Auto Collection", 100);
    await collectionTx.wait();
    console.log("Collection created successfully!");

    const collectionAddress = await main.getCollection(0);
    console.log(`Collection Contract address: ${collectionAddress}`);
    const collection = await ethers.getContractAt("Collection", collectionAddress);


    console.log("Fetching Pokemon Base Set cards...");
    let sets = await PokemonTCG.getAllSets();

    for (let i = 3; i < 6 && i < sets.length; i++) {
        const set_name = sets[i];

        let baseSetCards;
        try {
            baseSetCards = await PokemonTCG.findSetByID(set_name.id);
            console.log(`Fetched set: ${baseSetCards.name}`);
        } catch (error) {
            console.error("Error fetching base set: ", error);
            return;
        }

        const paramsV2 = { q: `set.id:${baseSetCards.id}` };
        let cards;
        try {
            cards = await PokemonTCG.findCardsByQueries(paramsV2);
            console.log(`Fetched ${cards.length} cards from Pokemon Base Set.`);
        } catch (error) {
            console.error("Error fetching cards: ", error);
            return;
        }


        for (let i = 0; i < 20 && i < cards.length; i++) {
            const card = cards[i];
            const cardNumber = parseInt(card.number);
            const imgURI = card.images.large;

            const mintTx = await main.mintCard(0, deployer, cardNumber, imgURI);
            await mintTx.wait();


            const salePrice = ethers.utils.parseEther("100");
            const collectionWithSigner = collection.connect(await ethers.getSigner(deployer));

            tokenId += 1;
            const setForSaleTx = await collectionWithSigner.setCardForSale(tokenId, salePrice);
            await setForSaleTx.wait();

        }


        console.log("Creating new Boosters instance...");
        const boostersTx = await main.createBoosters("Auto Booster Collection", 0);
        await boostersTx.wait();
        console.log("The example of Boosters was created successfully!");


        const boostersAddress = await main.getBoosters(0);
        console.log(`Boosters Contract address: ${boostersAddress}`);
        const boosters = await ethers.getContractAt("Boosters", boostersAddress);


        const boostersWithSigner = boosters.connect(await ethers.getSigner(deployer));


        for (let boosterIndex = 0; boosterIndex < 20; boosterIndex++) {
            const boosterImgURI = set_name.images.logo;
            console.log(`image addr ${boosterImgURI}`);
            const boosterCreateTx = await boostersWithSigner.mintBooster(deployer, boosterImgURI);
            await boosterCreateTx.wait();


            const boosterTokenId = await boostersWithSigner.totalSupply();



            for (let i = 0; i < 10 && i < cards.length; i++) {
                const randomIndex = Math.floor(Math.random() * (cards.length - 1));
                const card = cards[randomIndex];
                const cardNumber = parseInt(card.number);
                const imgURI = card.images.large;


                const addItemTx = await boostersWithSigner.addItemToBooster(boosterTokenId, cardNumber, imgURI);
                await addItemTx.wait();
                console.log(`Card number ${cardNumber} Added successfully to Booster ${boosterTokenId}，photo URL: ${imgURI}`);
            }


            const salePrice = ethers.utils.parseEther("500");
            const setForSaleTx = await boostersWithSigner.setBoosterForSale(boosterTokenId, salePrice);
            await setForSaleTx.wait();
            console.log(`Booster ${boosterIndex + 1} Has been successfully set for sale at the price of ${salePrice.toString()} wei！`);
        }
    }
};

export default setupCollectionsAndBoosters;
setupCollectionsAndBoosters.tags = ["SetupCollectionsAndBoosters"];