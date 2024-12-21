import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { WelcomePage } from './WelcomePage';
import { Register } from './Register';
import { MainPage } from './MainPage';
import AllUser from './AllUser';
import Boosters from './Boosters';
import CardStore from './CardStore';
import BoosterStore from './BoosterStore';

export const App = () => {
    return (
        <Router>
            <Routes>

                <Route path="/" element={<Navigate to="/welcome" />} />


                <Route path="/welcome" element={<WelcomePage />} />


                <Route path="/register" element={<Register />} />


                <Route path="/main" element={<MainPage />} />

                <Route path="/collection" element={<AllUser />} />
                <Route path="/boosters" element={<Boosters />} />
                <Route path="/cardStore" element={<CardStore />} />
                <Route path="/boosterStore" element={<BoosterStore />} />
            </Routes>
        </Router>
    );
};
