import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loginMessage, setLoginMessage] = useState("");
    const navigate = useNavigate(); // Хук для навигации на другую страницу

    const handleLogin = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/auth/v1/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                const errorData = await response.text();
                throw new Error(errorData);
            }

            const token = await response.text();
            localStorage.setItem('authToken', token);
            setLoginMessage("Успешная авторизация!");

            // Редирект на страницу с машинами
            navigate("/cars");
        } catch (error) {
            setLoginMessage(error.message);
        }
    };

    return (
        <div>
            <h2>Форма логина</h2>
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
            <p>{loginMessage}</p>
        </div>
    );
}

export default LoginPage;
