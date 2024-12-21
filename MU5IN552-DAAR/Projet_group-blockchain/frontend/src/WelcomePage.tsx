import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './WelcomePage.module.css';
import startPage from './photo/startPage.png';

export const WelcomePage = () => {
    const navigate = useNavigate();


    const handleImageClick = () => {
        navigate('/register');
    };

    return (
        <div className={styles.container}>
            <img
                src={startPage}
                alt="Welcome"
                className={styles.image}
                onClick={handleImageClick}
                style={{ cursor: 'pointer' }}
            />
        </div>
    );
};
