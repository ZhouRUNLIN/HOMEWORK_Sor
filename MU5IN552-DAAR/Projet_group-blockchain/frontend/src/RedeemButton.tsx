import React, { useState } from "react";
import { ethers } from "ethers";
import {Link, useNavigate} from 'react-router-dom';
import styles from './RedeemButton.module.css';
import MainABI from './abis/Main.json';
import BoostersABI from './abis/Boosters.json';
import boosterBackImage from './photo/booster_photo.png';

interface RedeemButtonProps {
    tokenId: string;
    boostersContractAddress: string;
    mainContractAddress: string;
}

interface CardInfo {
    cardNumber: string;
    imgURI: string;
}

const RedeemButton: React.FC<RedeemButtonProps> = ({ tokenId, boostersContractAddress, mainContractAddress }) => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [redeemResult, setRedeemResult] = useState<CardInfo[] | null>(null);
    const [redeemTxReceipt, setRedeemTxReceipt] = useState<any | null>(null);
    const [flippedCards, setFlippedCards] = useState<boolean[]>([]);

    const navigate = useNavigate();

    const handleRedeemClick = async () => {
        try {
            setLoading(true);
            setError(null);

            if (window.ethereum) {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();

                const boostersContract = new ethers.Contract(boostersContractAddress, BoostersABI, signer);

                const approveTx = await boostersContract.approve(mainContractAddress, tokenId);
                await approveTx.wait();
                console.log(`Booster ${tokenId} approved for ${mainContractAddress}`);

                const mainContract = new ethers.Contract(mainContractAddress, MainABI, signer);
                const redeemTx = await mainContract.redeemBooster(tokenId);
                const receipt = await redeemTx.wait();
                console.log(`Booster ${tokenId} redeemed.`);

                setRedeemTxReceipt(receipt);

                const itemCount = await boostersContract.getBoosterItemCount(tokenId);
                const cards: CardInfo[] = [];
                const flippedState: boolean[] = [];
                for (let i = 0; i < itemCount; i++) {
                    const [cardNumber, imgURI] = await boostersContract.getBoosterItem(tokenId, i);
                    cards.push({ cardNumber: cardNumber.toString(), imgURI });
                    flippedState.push(false);
                }
                setRedeemResult(cards);
                setFlippedCards(flippedState);
            } else {
                setError("MetaMask is not installed");
            }
        } catch (error) {
            console.error("Error redeeming booster:", error);
            setError("Failed to redeem booster. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    const handleCardClick = (index: number) => {
        const newFlippedState = [...flippedCards];
        newFlippedState[index] = !newFlippedState[index];
        setFlippedCards(newFlippedState);
    };

    const handleConfirmClick = async () => {
        try {
            if (redeemTxReceipt) {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const mainContract = new ethers.Contract(mainContractAddress, MainABI, signer);

                const confirmTx = await mainContract.confirmRedeem(tokenId);
                await confirmTx.wait();
                console.log(`Confirmed redemption for Booster ${tokenId}`);

                setRedeemResult(null);
                setRedeemTxReceipt(null);

                alert(`Booster ${tokenId} ownership has been transferred.`);

                navigate("/collection");
            }
        } catch (error) {
            console.error("Error confirming redemption:", error);
            setError("Failed to confirm redemption. Please try again.");
        }
    };

    return (
        <>
            {error && <p className={styles.error}>{error}</p>}
            {redeemResult ? (
                <div className={styles.resultContainer}>
                    <h2>Redeem Result</h2>
                    <div className={styles.cardsContainer}>
                        {redeemResult.map((card, index) => (
                            <div
                                key={index}
                                className={styles.cardItem}
                                onClick={() => handleCardClick(index)}
                            >
                                {flippedCards[index] ? (
                                    <img src={card.imgURI} alt={`Card ${card.cardNumber}`} className={styles.cardImage} />
                                ) : (
                                    <img src={boosterBackImage} alt="Card Back" className={styles.cardImage} />
                                )}
                            </div>
                        ))}
                    </div>
                    <Link to="/collection">
                        <button onClick={handleConfirmClick} className={styles.confirmButton}>
                            Confirm
                        </button>
                    </Link>

                </div>
            ) : (
                <button onClick={handleRedeemClick} className={styles.redeemButton} disabled={loading}>
                    {loading ? "Redeeming..." : "Redeem"}
                </button>
            )}
        </>
    );
};

export default RedeemButton;
