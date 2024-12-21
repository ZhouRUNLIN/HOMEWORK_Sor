import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import styles from './styles.module.css';


declare global {
    interface Window {
        ethereum: any;
    }
}

export const MainPage = () => {
    const [account, setAccount] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);


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
                console.error('Error connecting to MetaMask:', error);
                setErrorMessage('Error connecting to MetaMask.');
            }
        } else {
            setErrorMessage('MetaMask is not installed.');
        }
    };

    useEffect(() => {
        connectMetaMask();
    }, []);

    return (
        <div className={styles.body}>
            <h1>Welcome to My DApp</h1>
            {account ? (
                <div>
                    <p>Connected account: {account.slice(0, 6)}...{account.slice(-4)}</p>
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
            ) : (
                <div>
                    {errorMessage ? (
                        <p style={{ color: 'red' }}>{errorMessage}</p>
                    ) : (
                        <p>Connecting to MetaMask...</p>
                    )}
                </div>
            )}
        </div>
    );
};
