import React, { useEffect, useState } from "react";
import Web3 from "web3";
import { Link, useNavigate } from 'react-router-dom';
import styles from './Boosters.module.css';
import { ethers } from 'ethers';
import BoosterArtifact from './abis/Boosters.json';
import RedeemButton from './RedeemButton';

const Boosters: React.FC = () => {
    const [userAddress, setUserAddress] = useState<string | null>(null);
    const [boosters, setBoosters] = useState<Array<{ tokenId: string; name: string; image: string; price: string; isForSale: boolean }> | null>(null);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();


    const mainContractAddress = "0x5FbDB2315678afecb367f032d93F642f64180aa3";
    const boostersContractAddress = "0xB7A5bd0345EF1Cc5E66bf61BdeC17D2461fBd968";


    useEffect(() => {
        const getUserAddress = async () => {
            if (window.ethereum) {
                try {
                    const web3 = new Web3(window.ethereum);
                    const accounts = await web3.eth.getAccounts();
                    if (accounts.length > 0) {
                        setUserAddress(accounts[0]);
                    } else {
                        setError('No account found. Please connect to MetaMask.');
                    }
                } catch (error) {
                    console.error('Error fetching accounts:', error);
                    setError('Error fetching accounts from MetaMask.');
                }
            } else {
                setError('MetaMask is not installed.');
            }
        };

        getUserAddress();
    }, []);


    const fetchUserBoosters = async () => {
        if (userAddress) {
            try {
                const response = await fetch(`http://localhost:3000/api/boosters?userId=${userAddress}`);
                const data = await response.json();
                console.log('Fetched boosters data:', data);
                setBoosters(data);
            } catch (error) {
                console.error('Error fetching boosters:', error);
                setError('Error fetching boosters from server.');
            }
        }
    };

    useEffect(() => {

        fetchUserBoosters();


        const interval = setInterval(() => {
            fetchUserBoosters();
        }, 60000);


        return () => clearInterval(interval);
    }, [userAddress]);


    const sellBooster = async (tokenId: string) => {
        const price = prompt("请输入出售价格（ETH）：");

        if (price === null || price === '') {
            return;
        }

        try {
            const provider = new ethers.providers.Web3Provider(window.ethereum);
            const signer = provider.getSigner();
            const boosterContract = new ethers.Contract(boostersContractAddress, BoosterArtifact, signer);
            const priceInWei = ethers.utils.parseEther(price);

            const tx = await boosterContract.setBoosterForSale(tokenId, priceInWei, {
                gasLimit: ethers.utils.hexlify(300000)
            });

            await tx.wait();
            alert(`Booster ${tokenId} 已设置为待售，价格为 ${price} ETH`);

            fetchUserBoosters();
        } catch (error) {
            console.error('设置 Booster 出售时发生错误：', error);
            setError('设置 Booster 出售时发生错误。');
        }
    };


    const cancelBoosterSale = async (tokenId: string) => {
        try {
            const provider = new ethers.providers.Web3Provider(window.ethereum);
            const signer = provider.getSigner();
            const boosterContract = new ethers.Contract(boostersContractAddress, BoosterArtifact, signer);

            const tx = await boosterContract.cancelSale(tokenId, {
                gasLimit: ethers.utils.hexlify(300000)
            });

            await tx.wait();
            alert(`Booster ${tokenId} 的出售已取消`);

            fetchUserBoosters();
        } catch (error) {
            console.error('取消 Booster 出售时发生错误：', error);
            setError('取消 Booster 出售时发生错误。');
        }
    };


    const handleSellClick = (tokenId: string, isForSale: boolean) => {
        if (isForSale) {
            cancelBoosterSale(tokenId);
        } else {
            sellBooster(tokenId);
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

            <h1>My Boosters Collection</h1>
            {error ? (
                <p className={styles.error}>{error}</p>
            ) : userAddress ? (
                boosters ? (
                    boosters.length > 0 ? (
                        <div className={styles.cardsGrid}>
                            {boosters.map((booster, index) => (
                                <div key={index} className={styles.card}>
                                    <img src={booster.image} alt={booster.name} className={styles.cardImage} />
                                    <div className={styles.cardContent}>
                                        <h3>{booster.name}</h3>
                                        <p>Token ID: {booster.tokenId}</p>
                                        <p>Price: {booster.isForSale ? `${booster.price} ETH` : 'Not for sale'}</p>
                                        <button onClick={() => handleSellClick(booster.tokenId, booster.isForSale)}>
                                            {booster.isForSale ? "Cancel Sale" : "Sell this Booster"}
                                        </button>
                                        <RedeemButton
                                            tokenId={booster.tokenId}
                                            mainContractAddress={mainContractAddress}
                                            boostersContractAddress={boostersContractAddress}
                                        />
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p>No Boosters found for this user.</p>
                    )
                ) : (
                    <p>Loading Boosters...</p>
                )
            ) : (
                <p>Connecting to MetaMask...</p>
            )}
        </div>
    );
};

export default Boosters;
