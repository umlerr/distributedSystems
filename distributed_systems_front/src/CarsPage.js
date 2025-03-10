import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function CarsPage() {
    const [cars, setCars] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [totalCars, setTotalCars] = useState(0); // Общее количество машин
    const [showPopup, setShowPopup] = useState(false);
    const [errorMessage, setErrorMessage] = useState(""); // Для ошибки
    const [newCar, setNewCar] = useState({
        vin: "",
        brand: "",
        model: "",
        price: "",
        year: "",
        mileage: "",
    });

    const navigate = useNavigate();

    useEffect(() => {
        fetchCars(currentPage, pageSize);
    }, [currentPage, pageSize]);

    const fetchCars = async (page, size) => {
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                navigate("/");
                return;
            }

            const response = await fetch(`http://localhost:8081/api/cars/v1/cars?page=${page}&size=${size}`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                throw new Error(await response.text());
            }

            const carsData = await response.json();
            setCars(carsData.content || []);
            setTotalCars(carsData.totalElements); // Сохраняем общее количество машин
        } catch (error) {
            console.error("Ошибка при получении машин:", error);
        }
    };

    const handleAddCar = async () => {
        setErrorMessage(""); // Очистка предыдущей ошибки перед новой попыткой
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                navigate("/");
                return;
            }

            const response = await fetch("http://localhost:8081/api/cars/v1/add-car", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(newCar),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            setShowPopup(false);
            setNewCar({ vin: "", brand: "", model: "", price: "", year: "", mileage: "" });
            fetchCars(currentPage, pageSize);
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const totalPages = Math.ceil(totalCars / pageSize); // Количество страниц

    return (
        <div>
            <h2>Список машин</h2>
            <button className="btn btn-success" onClick={() => setShowPopup(true)}>
                Добавить машину
            </button>

            {/* Таблица машин */}
            <table className="table table-bordered">
                <thead>
                <tr>
                    <th>VIN</th>
                    <th>Бренд</th>
                    <th>Модель</th>
                    <th>Цена</th>
                    <th>Год</th>
                    <th>Пробег</th>
                </tr>
                </thead>
                <tbody>
                {cars.length > 0 ? (
                    cars.map((car, index) => (
                        <tr key={index}>
                            <td>{car.vin}</td>
                            <td>{car.brand}</td>
                            <td>{car.model}</td>
                            <td>{car.price}</td>
                            <td>{car.year}</td>
                            <td>{car.mileage}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="6">Машины не найдены</td>
                    </tr>
                )}
                </tbody>
            </table>

            {/* Пагинация */}
            <div className="pagination-container">
                <button
                    className="btn btn-secondary"
                    disabled={currentPage <= 0}
                    onClick={() => setCurrentPage(currentPage - 1)}
                >
                    Назад
                </button>
                <span>
                    Страница {currentPage + 1} из {totalPages}
                </span>
                <button
                    className="btn btn-secondary"
                    disabled={currentPage >= totalPages - 1}
                    onClick={() => setCurrentPage(currentPage + 1)}
                >
                    Вперед
                </button>
            </div>

            {/* Попап добавления машины */}
            {showPopup && (
                <div className="popup">
                    <div className="popup-inner">
                        <h3>Добавить новую машину</h3>
                        <input
                            type="text"
                            placeholder="VIN"
                            value={newCar.vin}
                            onChange={(e) => setNewCar({ ...newCar, vin: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Бренд"
                            value={newCar.brand}
                            onChange={(e) => setNewCar({ ...newCar, brand: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Модель"
                            value={newCar.model}
                            onChange={(e) => setNewCar({ ...newCar, model: e.target.value })}
                        />
                        <input
                            type="number"
                            placeholder="Цена"
                            value={newCar.price}
                            onChange={(e) => setNewCar({ ...newCar, price: e.target.value })}
                        />
                        <input
                            type="number"
                            placeholder="Год"
                            value={newCar.year}
                            onChange={(e) => setNewCar({ ...newCar, year: e.target.value })}
                        />
                        <input
                            type="number"
                            placeholder="Пробег"
                            value={newCar.mileage}
                            onChange={(e) => setNewCar({ ...newCar, mileage: e.target.value })}
                        />
                        <button className="btn btn-primary" onClick={handleAddCar}>
                            Добавить
                        </button>
                        <button className="btn btn-danger" onClick={() => setShowPopup(false)}>
                            Отмена
                        </button>

                        {errorMessage && (
                            <div className="error-message">
                                <p>{errorMessage}</p>
                            </div>
                        )}
                    </div>
                </div>
            )}

            <style>{`
                .error-message {
                    color: red;
                    font-weight: bold;
                    margin-top: 10px;
                }
            `}</style>

            <style>{`
                .popup {
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0, 0, 0, 0.5);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
                .popup-inner {
                    background: white;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
                    display: flex;
                    flex-direction: column;
                    gap: 10px;
                }
                .popup-inner input {
                    padding: 8px;
                    font-size: 16px;
                    width: 100%;
                    border: 1px solid #ccc;
                    border-radius: 5px;
                }
                .popup-inner button {
                    margin-top: 10px;
                }
                .popup-inner .error-message {
                    color: red;
                    font-size: 14px;
                }
            `}</style>
        </div>
    );
}

export default CarsPage;
