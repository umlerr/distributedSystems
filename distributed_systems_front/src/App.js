import React, { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, useNavigate } from "react-router-dom";
import LoginPage from "./LoginPage";
import CarsPage from "./CarsPage";

function PrivateRoute({ children }) {
    const navigate = useNavigate();
    const token = localStorage.getItem("authToken");

    useEffect(() => {
        if (!token) navigate("/");
    }, [token, navigate]);

    return token ? children : null;
}

function AuthRedirect() {
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("authToken");
        if (token) {
            navigate("/cars");
        } else {
            navigate("/login");
        }
    }, [navigate]);

    return null;
}

function App() {
    return (
        <Router>
            <div className="container" style={{ textAlign: "center", marginTop: "50px" }}>
                <h1>React + Spring Boot</h1>
                <Routes>
                    <Route path="/" element={<AuthRedirect />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/cars" element={<PrivateRoute><CarsPage /></PrivateRoute>} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
