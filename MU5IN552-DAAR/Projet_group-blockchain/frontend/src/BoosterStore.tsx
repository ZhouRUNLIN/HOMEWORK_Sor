import React, { useEffect, useState } from 'react';
import { ethers } from 'ethers';
import styles from './BoosterStore.module.css';
import BoostersABI from './abis/Boosters.json';
import {Link} from "react-router-dom";

const BoosterStore: React.FC = () => {
    const [boosters, setBoosters] = useState<Array<any> | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [account, setAccount] = useState<string | null>(null);


    const fetchForSaleBoosters = async () => {
        try {
            const response = await fetch('http://localhost:3000/api/boosters/for-sale');
            const data = await response.json();
            console.log('Fetched boosters:', data);
            setBoosters(data);
        } catch (error) {
            console.error('Error fetching boosters:', error);
            setError('Error fetching boosters from server.');
        }
    };

    useEffect(() => {
        fetchForSaleBoosters();

        const connectMetaMask = async () => {
            if (window.ethereum) {
                try {
                    const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
                    if (accounts.length > 0) {
                        setAccount(accounts[0]);
                    } else {
                        setError('No accounts found.');
                    }
                } catch (error) {
                    console.error('Error connecting to MetaMask:', error);
                    setError('Error connecting to MetaMask.');
                }
            } else {
                setError('MetaMask is not installed.');
            }
        };

        connectMetaMask();


        const interval = setInterval(() => {
            fetchForSaleBoosters();
        }, 500);


        return () => clearInterval(interval);
    }, []);


    const handleBuyClick = async (booster: any) => {
        try {
            if (!account) {
                setError('Please connect your wallet.');
                return;
            }

            const provider = new ethers.providers.Web3Provider(window.ethereum);
            const signer = provider.getSigner();

            const boosterAddress = booster.boosterAddress;
            const boosterContract = new ethers.Contract(boosterAddress, BoostersABI, signer);

            const priceInWei = ethers.BigNumber.from(booster.price);

            const tx = await boosterContract.buyBooster(booster.tokenId, {
                value: priceInWei,
                gasLimit: ethers.utils.hexlify(300000)
            });

            await tx.wait();

            alert(`Booster ${booster.tokenId} purchased successfully!`);
            setError(null);


            fetchForSaleBoosters();
        } catch (error) {
            console.error(`Error buying booster ${booster.tokenId}:`, error);
            setError(`Failed to purchase booster ${booster.tokenId}.`);
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
            <h1>Booster Store</h1>
            {error && <p className={styles.error}>{error}</p>}
            {account ? (
                boosters ? (
                    boosters.length > 0 ? (
                        <div className={styles.boostersGrid}>
                            {boosters.map((booster, index) => (
                                <div key={index} className={styles.booster}>
                                    <img src={booster.image} alt={booster.name} className={styles.boosterImage} />
                                    <div className={styles.boosterContent}>
                                        <h3>{booster.name}</h3>
                                        <p>Token ID: {booster.tokenId}</p>
                                        <p>Price: {ethers.utils.formatEther(booster.price)} ETH</p>
                                        <button onClick={() => handleBuyClick(booster)}>
                                            Buy this booster
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p>No boosters found for sale.</p>
                    )
                ) : (
                    <p>Loading boosters for sale...</p>
                )
            ) : (
                <p>Please connect your wallet.</p>
            )}
        </div>
    );
};

export default BoosterStore;
