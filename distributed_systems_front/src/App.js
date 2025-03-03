import React, { useState, useEffect } from "react";
import './styles.css';

function App() {
    const [message, setMessage] = useState("Загрузка...");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loginMessage, setLoginMessage] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/users/v1/login")
            .then(response => response.text())
            .then(data => setMessage(data))
            .catch(error => console.error("Ошибка:", error));
    }, []);

    const handleLogin = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/users/v1/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            const message = await response.text();
            setLoginMessage(message);

            if (!response.ok) {
                throw new Error(message);
            }
        } catch (error) {
            setLoginMessage(error.message);
        }
    };

    return (
        <div className="container" style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>React + Spring Boot</h1>

            {/* GET /api/users/v1/login */}
            <h2>{message}</h2>

            {/* Форма логина с кастомными стилями */}
            <div className="form-container">
                <div className="mb-3">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Логин"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
                <div className="mb-3">
                    <input
                        type="password"
                        className="form-control"
                        placeholder="Пароль"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button className="btn btn-primary" onClick={handleLogin}>Войти</button>
            </div>

            {/* Ответ от POST /api/users/v1/login */}
            <p>{loginMessage}</p>
        </div>
    );
}

export default App;
