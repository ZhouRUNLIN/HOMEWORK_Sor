// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/Strings.sol";
import "@openzeppelin/contracts/utils/Base64.sol";

contract Collection is ERC721, Ownable {
    using Strings for uint256;

    uint256 public cardCount;
    uint256 private _tokenIdCounter;
    string public collectionName;


    mapping(uint256 => bool) public tokenExists;


    struct CardDetails {
        bool isForSale;
        int256 price;
    }

    mapping(uint256 => uint256) public cardNumbers;
    mapping(uint256 => string) public imgURIs;
    mapping(uint256 => CardDetails) public cardDetails;


    constructor(string memory _name, uint256 _cardCount, address owner_) ERC721(_name, "CARD") Ownable(msg.sender) {
        collectionName = _name;
        cardCount = _cardCount;
        _tokenIdCounter = 1;
        _transferOwnership(owner_);
    }


    function mint(address to, uint256 cardNumber, string memory imgURI) external onlyOwner {
        uint256 tokenId = _tokenIdCounter++;
        _safeMint(to, tokenId);
        cardNumbers[tokenId] = cardNumber;
        imgURIs[tokenId] = imgURI;


        cardDetails[tokenId] = CardDetails({
            isForSale: false,
            price: -1
        });


        tokenExists[tokenId] = true;
    }


    function setCardForSale(uint256 tokenId, int256 price) external {
        require(tokenExists[tokenId], "Card does not exist");
        require(ownerOf(tokenId) == msg.sender, "Only card owner can set for sale");
        require(price > 0, "Price must be greater than 0");
        cardDetails[tokenId].isForSale = true;
        cardDetails[tokenId].price = price;
    }


    function cancelSale(uint256 tokenId) external {
        require(tokenExists[tokenId], "Card does not exist");
        require(ownerOf(tokenId) == msg.sender, "Only card owner can cancel sale");
        cardDetails[tokenId].isForSale = false;
        cardDetails[tokenId].price = -1;
    }


    function buyCard(uint256 tokenId) external payable {
        require(tokenExists[tokenId], "Card does not exist");
        CardDetails storage details = cardDetails[tokenId];
        require(details.isForSale, "Card is not for sale");
        require(msg.value >= uint256(details.price), "Insufficient payment");

        address previousOwner = ownerOf(tokenId);
        require(previousOwner != msg.sender, "Cannot buy your own card");


        _transfer(previousOwner, msg.sender, tokenId);


        details.isForSale = false;
        details.price = -1;


        (bool sent, ) = payable(previousOwner).call{value: msg.value}("");
        require(sent, "Failed to send Ether");
    }


    function tokenURI(uint256 tokenId) public view override returns (string memory) {
        require(tokenExists[tokenId], "Card does not exist");
        string memory json = _buildTokenURI(tokenId);
        return string(abi.encodePacked("data:application/json;base64,", json));
    }


    function _buildTokenURI(uint256 tokenId) private view returns (string memory) {
        CardDetails memory details = cardDetails[tokenId];
        string memory cardNumberStr = cardNumbers[tokenId].toString();
        string memory ownerAddress = Strings.toHexString(uint256(uint160(ownerOf(tokenId))), 20);
        string memory isForSaleStr = details.isForSale ? "Yes" : "No";
        string memory priceStr = details.price > 0 ? Strings.toString(uint256(details.price)) : "Not for sale";

        return Base64.encode(
            bytes(
                abi.encodePacked(
                    '{"name":"',
                    name(),
                    ' #',
                    cardNumberStr,
                    '", "description":"',
                    collectionName,
                    ' Collection Card", "image":"',
                    imgURIs[tokenId],
                    '", "attributes":['
                    '{"trait_type":"Card Number","value":"', cardNumberStr, '"},',
                    '{"trait_type":"For Sale","value":"', isForSaleStr, '"},',
                    '{"trait_type":"Price","value":"', priceStr, '"},',
                    '{"trait_type":"Owner","value":"', ownerAddress, '"}'
                ']}'
                )
            )
        );
    }
}