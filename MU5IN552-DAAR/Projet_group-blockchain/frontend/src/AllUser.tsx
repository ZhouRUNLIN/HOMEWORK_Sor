import React, { useEffect, useState } from "react";
import { ethers } from 'ethers';
import {Link, useNavigate} from 'react-router-dom';
import styles from './AllUser.module.css';
import CollectionABI from './abis/Collection.json';

declare global {
  interface Window {
    ethereum: any;
  }
}

const AllUser: React.FC = () => {
  const [userAddress, setUserAddress] = useState<string | null>(null);
  const [nfts, setNFTs] = useState<Array<{
    tokenId: string;
    name: string;
    image: string;
    price: string;
    isForSale: boolean;
    collectionAddress: string;
  }> | null>(null);
  const [favorites, setFavorites] = useState<Array<{
    tokenId: string;
    name: string;
    image: string;
    price: string;
    isForSale: boolean;
    collectionAddress: string;
  }> | []>([]);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [showFavorites, setShowFavorites] = useState<boolean>(false);
  const navigate = useNavigate();


  const connectMetaMask = async () => {
    if (typeof window.ethereum !== 'undefined') {
      try {
        const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
        if (accounts.length > 0) {
          setUserAddress(accounts[0]);
        } else {
          setErrorMessage('No accounts found.');
        }
      } catch (error) {
        console.error("Error connecting to MetaMask:", error);
        setErrorMessage('Error connecting to MetaMask.');
      }
    } else {
      setErrorMessage('MetaMask is not installed.');
    }
  };

  useEffect(() => {
    connectMetaMask();
  }, []);


  const fetchUserNFTs = async () => {
    if (userAddress) {
      try {
        const response = await fetch(`http://localhost:3000/api/cards?userId=${userAddress}`);
        const data = await response.json();
        console.log('Fetched data:', data);
        setNFTs(data);
      } catch (error) {
        console.error('Error fetching NFTs:', error);
        setErrorMessage('Error fetching NFTs from the server.');
      }
    }
  };

  useEffect(() => {
    fetchUserNFTs();


    const interval = setInterval(() => {
      fetchUserNFTs();
    }, 1000);


    return () => clearInterval(interval);
  }, [userAddress]);


  const setCardForSale = async (card: any) => {
    if (!userAddress) {
      setErrorMessage('No account connected.');
      return;
    }

    const price = prompt("Enter sale price (in ETH):");

    if (price === null || price === '') {
      return;
    }

    try {
      const priceInWei = ethers.utils.parseEther(price);

      const provider = new ethers.providers.Web3Provider(window.ethereum);
      const signer = provider.getSigner();

      const collectionContract = new ethers.Contract(card.collectionAddress, CollectionABI, signer);

      const tx = await collectionContract.setCardForSale(card.tokenId, priceInWei, {
        gasLimit: ethers.utils.hexlify(300000)
      });

      await tx.wait();

      alert(`Card ${card.tokenId} is listed for sale at ${price} ETH`);
      fetchUserNFTs();
    } catch (error) {
      console.error("Error setting card for sale:", error);
      setErrorMessage("Error setting card for sale.");
    }
  };


  const cancelSale = async (card: any) => {
    if (!userAddress) {
      setErrorMessage('No account connected.');
      return;
    }

    try {
      const provider = new ethers.providers.Web3Provider(window.ethereum);
      const signer = provider.getSigner();

      const collectionContract = new ethers.Contract(card.collectionAddress, CollectionABI, signer);

      const tx = await collectionContract.cancelSale(card.tokenId, {
        gasLimit: ethers.utils.hexlify(300000)
      });

      await tx.wait();

      alert(`Sale of card ${card.tokenId} has been canceled`);
      fetchUserNFTs();
    } catch (error) {
      console.error("Error canceling card sale:", error);
      setErrorMessage("Error canceling card sale.");
    }
  };


  const toggleFavorite = (nft: any) => {
    if (favorites.some(fav => fav.tokenId === nft.tokenId)) {
      setFavorites(favorites.filter(fav => fav.tokenId !== nft.tokenId));
    } else {
      setFavorites([...favorites, nft]);
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
        <h1>My NFT Collection</h1>
        {errorMessage ? (
            <p className={styles.error}>{errorMessage}</p>
        ) : userAddress ? (
            nfts ? (
                <>
                  {/* Favorites button with hover to show favorite cards */}
                  <div
                      className={styles.favoritesWrapper}
                      onMouseEnter={() => setShowFavorites(true)}
                      onMouseLeave={() => setShowFavorites(false)}
                  >
                    <button className={styles.favoritesButton}>
                      View Favorites ({favorites.length})
                    </button>
                    {showFavorites && (
                        <div className={styles.favoritesList}>
                          {favorites.length > 0 ? (
                              favorites.map((fav, index) => (
                                  <div key={index} className={styles.favoriteItem}>
                                    <img src={fav.image} alt={fav.name} className={styles.favoriteImage} />
                                    <p>{fav.name}</p>
                                  </div>
                              ))
                          ) : (
                              <p>No favorites yet.</p>
                          )}
                        </div>
                    )}
                  </div>

                  {nfts.length > 0 ? (
                      <div className={styles.cardsGrid}>
                        {nfts.map((nft, index) => (
                            <div key={index} className={styles.card}>
                              <div className={styles.cardHeader}>
                                {/* Favorite button */}
                                <button
                                    className={styles.starButton}
                                    onClick={() => toggleFavorite(nft)}
                                >
                                  {favorites.some(fav => fav.tokenId === nft.tokenId) ? "★" : "☆"}
                                </button>
                              </div>
                              <img src={nft.image} alt={nft.name} className={styles.cardImage} />
                              <div className={styles.cardContent}>
                                <h3>{nft.name}</h3>
                                <p>Token ID: {nft.tokenId}</p>
                                {nft.isForSale && <p>Price: {ethers.utils.formatEther(nft.price)} ETH</p>}
                                {nft.isForSale ? (
                                    <button onClick={() => cancelSale(nft)}>Cancel Sale</button>
                                ) : (
                                    <button onClick={() => setCardForSale(nft)}>Sell this card</button>
                                )}
                              </div>
                            </div>
                        ))}
                      </div>
                  ) : (
                      <p>No NFTs found for this user.</p>
                  )}
                </>
            ) : (
                <p>Loading NFTs...</p>
            )
        ) : (
            <p>Connecting to MetaMask...</p>
        )}
      </div>
  );
};

export default AllUser;
