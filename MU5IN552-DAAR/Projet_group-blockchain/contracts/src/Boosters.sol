// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Boosters is ERC721, Ownable {
    using Strings for uint256;

    uint256 private _tokenIdCounter;
    string public collectionName;
    uint256 public collectionId;


    struct CardInfo {
        uint256 cardNumber;
        string imgURI;
    }


    mapping(uint256 => CardInfo[]) public boosterContents;

    mapping(uint256 => string) public imgURIs;
    mapping(uint256 => ItemDetails) public itemDetails;


    struct ItemDetails {
        bool isForSale;
        int256 price;
    }


    event BoosterCreated(uint256 indexed boosterId, string imgURI, address owner);
    event ItemAddedToBooster(uint256 indexed boosterId, uint256 cardNumber);
    event ItemForSale(uint256 indexed tokenId, int256 price);
    event SaleCancelled(uint256 indexed tokenId);
    event BoosterSold(uint256 indexed tokenId, address indexed buyer, uint256 price);

    constructor(string memory _name, address initialOwner, uint256 _collectionId) ERC721(_name, "BOOSTER") Ownable(msg.sender) {
        collectionName = _name;
        _tokenIdCounter = 1;
        collectionId = _collectionId;
        _transferOwnership(initialOwner);
    }

    /**
     * @dev 铸造新的 Booster
     * @param to 接收者地址
     * @param imgURI Booster 的图片 URI
     */
    function mintBooster(address to, string memory imgURI) external onlyOwner {
        uint256 tokenId = _tokenIdCounter++;
        _safeMint(to, tokenId);
        imgURIs[tokenId] = imgURI;

        emit BoosterCreated(tokenId, imgURI, to);
    }

    /**
     * @dev 向指定的 Booster 中添加物品
     * @param boosterId Booster 的 tokenId
     * @param cardNumber 要添加的卡牌编号
     * @param imgURI 卡牌的图片 URI
     */
    function addItemToBooster(uint256 boosterId, uint256 cardNumber, string calldata imgURI) external onlyOwner {
        boosterContents[boosterId].push(CardInfo({cardNumber: cardNumber, imgURI: imgURI}));
        emit ItemAddedToBooster(boosterId, cardNumber);
    }

    /**
     * @dev 获取指定 Booster 中的物品数量
     * @param boosterId Booster 的 tokenId
     * @return 物品数量
     */
    function getBoosterItemCount(uint256 boosterId) external view returns (uint256) {
        return boosterContents[boosterId].length;
    }

    /**
     * @dev 获取指定 Booster 中的某个物品信息
     * @param boosterId Booster 的 tokenId
     * @param index 物品索引
     * @return cardNumber 卡牌编号
     * @return imgURI 卡牌图片 URI
     */
    function getBoosterItem(uint256 boosterId, uint256 index) external view returns (uint256 cardNumber, string memory imgURI) {
        CardInfo memory item = boosterContents[boosterId][index];
        return (item.cardNumber, item.imgURI);
    }

    /**
     * @dev 设置 Booster 为待售状态
     * @param tokenId Booster 的 tokenId
     * @param price 销售价格（单位：wei）
     */
    function setBoosterForSale(uint256 tokenId, int256 price) external {
        require(ownerOf(tokenId) == msg.sender, "Boosters: Only owner can set for sale");
        require(price > 0, "Boosters: Price must be greater than 0");
        itemDetails[tokenId].isForSale = true;
        itemDetails[tokenId].price = price;

        emit ItemForSale(tokenId, price);
    }

    /**
     * @dev 取消 Booster 的出售状态
     * @param tokenId Booster 的 tokenId
     */
    function cancelSale(uint256 tokenId) external {
        require(ownerOf(tokenId) == msg.sender, "Boosters: Only owner can cancel sale");
        itemDetails[tokenId].isForSale = false;
        itemDetails[tokenId].price = -1;

        emit SaleCancelled(tokenId);
    }

    /**
     * @dev 购买待售的 Booster
     * @param tokenId Booster 的 tokenId
     */
    function buyBooster(uint256 tokenId) external payable {
        ItemDetails storage details = itemDetails[tokenId];
        require(details.isForSale, "Boosters: Booster is not for sale");
        require(msg.value >= uint256(details.price), "Boosters: Insufficient payment");

        address previousOwner = ownerOf(tokenId);
        require(previousOwner != msg.sender, "Boosters: Cannot buy your own booster");


        _transfer(previousOwner, msg.sender, tokenId);


        details.isForSale = false;
        details.price = -1;


        (bool sent, ) = payable(previousOwner).call{value: msg.value}("");
        require(sent, "Boosters: Failed to send Ether");

        emit BoosterSold(tokenId, msg.sender, msg.value);
    }

    /**
     * @dev 获取 Booster 的总数
     * @return Booster 总数
     */
    function totalSupply() external view returns (uint256) {
        return _tokenIdCounter - 1;
    }


}