import React, { useEffect, useState } from 'react';
import { ethers } from 'ethers';
import styles from './CardStore.module.css';
import MainABI from './abis/Main.json';
import CollectionABI from './abis/Collection.json';
import {Link} from "react-router-dom";

declare global {
    interface Window {
        ethereum: any;
    }
}

const CardStore: React.FC = () => {
    const [cards, setCards] = useState<Array<{
        tokenId: string;
        name: string;
        image: string;
        price: string;
        collectionAddress: string;
    }> | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [account, setAccount] = useState<string | null>(null);


    const connectMetaMask = async () => {
        if (typeof window.ethereum !== 'undefined') {
            try {
                const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
                if (accounts.length > 0) {
                    setAccount(accounts[0]);
                } else {
                    setErrorMessage('No account found.');
                }
            } catch (error) {
                console.error("Error connecting to MetaMask:", error);
                setErrorMessage('Error connecting to MetaMask.');
            }
        } else {
            setErrorMessage('MetaMask is not installed.');
        }
    };


    const fetchForSaleCards = async () => {
        try {
            const response = await fetch('http://localhost:3000/api/cards/for-sale');
            const data = await response.json();
            console.log('Fetched cards:', data);
            setCards(data);
        } catch (error) {
            console.error('Error fetching cards:', error);
            setErrorMessage('Error fetching cards from server.');
        }
    };

    useEffect(() => {
        connectMetaMask();


        const interval = setInterval(() => {
            fetchForSaleCards();
        }, 1000);


        return () => clearInterval(interval);
    }, []);


    const handleBuyClick = async (collectionAddress: string, tokenId: string, price: string) => {
        if (!account) {
            setErrorMessage('No account connected.');
            return;
        }

        try {
            const provider = new ethers.providers.Web3Provider(window.ethereum);
            const signer = provider.getSigner();


            const collectionContract = new ethers.Contract(collectionAddress, CollectionABI, signer);

            const priceInWei = ethers.BigNumber.from(price);


            const tx = await collectionContract.buyCard(tokenId, {
                value: priceInWei,
                gasLimit: ethers.utils.hexlify(300000),
            });
            await tx.wait();

            alert(`Card ${tokenId} purchased successfully!`);
            setErrorMessage(null);


            fetchForSaleCards();
        } catch (error) {
            console.error(`Error buying card ${tokenId}:`, error);
            setErrorMessage(`Failed to purchase card ${tokenId}.`);
        }
    };

    return (
        <div className={styles.container}>
            <h1>Welcome to My DApp</h1>
            <div>

                <Link to="/collection">
                    <button>My Collection</button>
                </Link>
                <Link to="/boosters">
                    <button>My Booster</button>
                </Link>
                <Link to="/cardStore">
                    <button>Card Store</button>
                </Link>
                <Link to="/boosterStore">
                    <button>Booster Store</button>
                </Link>
            </div>
            <h1>Card Store</h1>
            {errorMessage && <p className={styles.error}>{errorMessage}</p>}
            {cards ? (
                cards.length > 0 ? (
                    <div className={styles.cardsGrid}>
                        {cards.map((card, index) => (
                            <div key={index} className={styles.card}>
                                <img src={card.image} alt={card.name} className={styles.cardImage} />
                                <div className={styles.cardContent}>
                                    <h3>{card.name}</h3>
                                    <p>Token ID: {card.tokenId}</p>
                                    <p>Price: {ethers.utils.formatEther(card.price)} ETH</p>
                                    <button onClick={() => handleBuyClick(card.collectionAddress, card.tokenId, card.price)}>
                                        Buy this card
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>No cards found for sale.</p>
                )
            ) : (
                <p>Loading cards for sale...</p>
            )}
        </div>
    );
};

export default CardStore;
