// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./Collection.sol";
import "./Boosters.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Main is Ownable {
    uint256 private collectionCount;
    uint256 private boostersCount;


    mapping(uint256 => Collection) public collections;


    mapping(uint256 => Boosters) public boosters;


    event CollectionCreated(
        uint256 indexed collectionId,
        address collectionAddress,
        string name,
        uint256 cardCount
    );

    event BoostersCreated(
        uint256 indexed boostersId,
        address boostersAddress,
        string name
    );

    event BoosterMinted(
        uint256 indexed boostersId,
        uint256 tokenId,
        address owner,
        string imgURI
    );

    event ItemAddedToBooster(
        uint256 indexed boostersId,
        uint256 boosterTokenId,
        uint256 cardNumber
    );

    event ItemForSale(
        uint256 indexed boostersId,
        uint256 boosterTokenId,
        int256 price
    );

    event SaleCancelled(
        uint256 indexed boostersId,
        uint256 boosterTokenId
    );

    event BoosterSold(
        uint256 indexed boostersId,
        uint256 boosterTokenId,
        address indexed buyer,
        uint256 price
    );

    event BoosterRedeemed(
        uint256 indexed boostersId,
        uint256 indexed boosterTokenId,
        address indexed redeemer
    );


    constructor() Ownable(msg.sender)  {
        collectionCount = 0;
        boostersCount = 0;
        _transferOwnership(msg.sender);
    }


    function createCollection(
        string calldata name,
        uint256 cardCount
    ) external onlyOwner {
        Collection collection = new Collection(
            name,
            cardCount,
            address(this)
        );
        collections[collectionCount] = collection;
        emit CollectionCreated(collectionCount, address(collection), name, cardCount);
        collectionCount++;
    }


    function mintCard(
        uint256 collectionId,
        address to,
        uint256 cardNumber,
        string calldata imgURI
    ) external onlyOwner {
        Collection collection = collections[collectionId];
        collection.mint(to, cardNumber, imgURI);
    }


    function setCardForSale(
        uint256 collectionId,
        uint256 tokenId,
        int256 price
    ) external {
        Collection collection = collections[collectionId];
        collection.setCardForSale(tokenId, price);
    }


    function cancelSale(
        uint256 collectionId,
        uint256 tokenId
    ) external {
        Collection collection = collections[collectionId];
        collection.cancelSale(tokenId);
    }


    function createBoosters(
        string calldata name,
        uint256 collectionId
    ) external onlyOwner {
        Boosters boostersInstance = new Boosters(name, msg.sender, collectionId);
        boosters[boostersCount] = boostersInstance;
        emit BoostersCreated(boostersCount, address(boostersInstance), name);
        boostersCount++;
    }


    function createBooster(
        uint256 boostersId,
        address to,
        string calldata imgURI
    ) external onlyOwner {
        Boosters boostersInstance = boosters[boostersId];
        boostersInstance.mintBooster(to, imgURI);
        uint256 boosterTokenId = boostersInstance.totalSupply();
        emit BoosterMinted(boostersId, boosterTokenId, to, imgURI);
    }


    function addItemToBooster(
        uint256 boostersId,
        uint256 boosterTokenId,
        uint256 cardNumber,
        string calldata imgURI
    ) external onlyOwner {
        Boosters boostersInstance = boosters[boostersId];
        boostersInstance.addItemToBooster(boosterTokenId, cardNumber, imgURI);
        emit ItemAddedToBooster(boostersId, boosterTokenId, cardNumber);
    }


    function setBoosterForSale(
        uint256 boostersId,
        uint256 boosterTokenId,
        int256 price
    ) external {
        Boosters boostersInstance = boosters[boostersId];
        boostersInstance.setBoosterForSale(boosterTokenId, price);
        emit ItemForSale(boostersId, boosterTokenId, price);
    }


    function cancelBoosterSale(
        uint256 boostersId,
        uint256 boosterTokenId
    ) external {
        Boosters boostersInstance = boosters[boostersId];
        boostersInstance.cancelSale(boosterTokenId);
        emit SaleCancelled(boostersId, boosterTokenId);
    }


    function buyBooster(
        uint256 boostersId,
        uint256 boosterTokenId
    ) external payable {
        Boosters boostersInstance = boosters[boostersId];
        boostersInstance.buyBooster{value: msg.value}(boosterTokenId);
        emit BoosterSold(boostersId, boosterTokenId, msg.sender, msg.value);
    }


    function redeemBooster(uint256 boosterTokenId) external {

        Boosters boostersInstance = boosters[0];
        require(boostersInstance.ownerOf(boosterTokenId) == msg.sender, "Main: Only booster owner can redeem");


        boostersInstance.transferFrom(msg.sender, owner(), boosterTokenId);


        uint256 collectionId = boostersInstance.collectionId();
        Collection collection = collections[collectionId];

        uint256 itemCount = boostersInstance.getBoosterItemCount(boosterTokenId);
        for (uint256 i = 0; i < itemCount; i++) {
            (uint256 cardNumber, string memory imgURI) = boostersInstance.getBoosterItem(boosterTokenId, i);


            collection.mint(msg.sender, cardNumber, imgURI);
        }



        emit BoosterRedeemed(0, boosterTokenId, msg.sender);
    }




    function getCollection(uint256 collectionId) external view returns (address) {
        return address(collections[collectionId]);
    }


    function getBoosters(uint256 boostersId) external view returns (address) {
        return address(boosters[boostersId]);
    }


    function totalCollections() external view returns (uint256) {
        return collectionCount;
    }


    function totalBoosters() external view returns (uint256) {
        return boostersCount;
    }
}