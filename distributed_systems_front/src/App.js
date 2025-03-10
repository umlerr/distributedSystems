import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LoginPage from "./LoginPage";
import CarsPage from "./CarsPage";

function App() {
    return (
        <Router>
            <div className="container" style={{ textAlign: "center", marginTop: "50px" }}>
                <h1>React + Spring Boot</h1>
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path="/cars" element={<CarsPage />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
