import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Register.module.css';
import startPage from './photo/register.png';
import pkData from './photo/PK.json';

export const Register = () => {
    const [privateKey, setPrivateKey] = useState<string>('');
    const navigate = useNavigate();


    const getRandomPrivateKey = () => {
        const keys = pkData.PRIVATE_KEYS;
        const randomKey = keys[Math.floor(Math.random() * keys.length)];
        setPrivateKey(randomKey);
    };

    useEffect(() => {
        getRandomPrivateKey();
    }, []);


    const handleImageClick = () => {
        alert(`Your Private Key: ${privateKey}`);
        navigate('/main');
    };

    return (
        <div className={styles.container}>
            <img
                src={startPage}
                alt="Register"
                className={styles.image}
                onClick={handleImageClick}
                style={{ cursor: 'pointer' }}
            />
        </div>
    );
};
