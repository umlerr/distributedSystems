import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function CarsPage() {
    const [cars, setCars] = useState([]);
    const [carDetails, setCarDetails] = useState({});
    const [vinSuggestions, setVinSuggestions] = useState([]);
    const [isVinExist, setIsVinExist] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [totalCars, setTotalCars] = useState(0);
    const [showPopup, setShowPopup] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
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

            const response = await fetch(
                `http://localhost:8082/api/listings/v1/catalog/listings?page=${page}&size=${size}`,
                {
                    method: "GET",
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            if (!response.ok) {
                throw new Error(await response.text());
            }

            const carsData = await response.json();
            setCars(carsData.content || []);
            setTotalCars(carsData.totalElements);
            carsData.content.forEach((car) => fetchCarDetails(car.carId));
        } catch (error) {
            console.error("Ошибка при получении списка машин:", error);
        }
    };

    const fetchCarDetails = async (carId) => {
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                navigate("/");
                return;
            }

            const response = await fetch(
                `http://localhost:8081/api/cars/v1/car-by-id/${carId}`,
                {
                    method: "GET",
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            if (!response.ok) {
                throw new Error(await response.text());
            }

            const carData = await response.json();
            setCarDetails((prevDetails) => ({ ...prevDetails, [carId]: carData }));
        } catch (error) {
            console.error(`Ошибка при получении данных машины ${carId}:`, error);
        }
    };

    const handleAddCar = async (e) => {
        e.preventDefault(); // Останавливаем перезагрузку страницы

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
                // Если сервер вернул ошибку, извлекаем текст ошибки
                const errorText = await response.text();

                // Если код ответа 409 (Conflict), то выводим ошибку, что VIN уже существует
                if (response.status === 409) {
                    setErrorMessage(errorText || "Машина с таким VIN номером уже существует");
                } else {
                    // Для других ошибок показываем стандартное сообщение
                    throw new Error(errorText || "Произошла неизвестная ошибка.");
                }
            } else {
                // Если все успешно
                setShowPopup(false);
                setNewCar({ vin: "", brand: "", model: "", price: "", year: "", mileage: "" });
                fetchCars(currentPage, pageSize);
            }
        } catch (error) {
            console.error("Ошибка:", error);
            if (!errorMessage) {
                setErrorMessage("Произошла ошибка при добавлении автомобиля.");
            }
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("authToken");
        navigate("/");
    };


    const totalPages = Math.ceil(totalCars / pageSize);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('ru-RU');
    };

    const formatPrice = (price) => {
        return Math.floor(price).toLocaleString();
    };

    return (
        <div className="container">
            <h2>Список объявлений</h2>

            <div className="button-container">
                <button className="btn btn-success" onClick={() => setShowPopup(true)}>
                    Добавить объявление
                </button>
                <button className="btn btn-danger" onClick={handleLogout}>
                    Выйти
                </button>
            </div>

            {/* Всплывающее окно */}
            {showPopup && (
                <div className="popup-overlay">
                    <div className="popup">
                        <h3>Добавить новое объявление</h3>
                        <input
                            type="text"
                            placeholder="VIN"
                            value={newCar.vin}
                        />
                        <input
                            type="text"
                            placeholder="Марка"
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
                        {errorMessage && <div className="error-message">{errorMessage}</div>}
                        <div className="popup-buttons">
                            <button className="btn btn-success" onClick={handleAddCar}>
                                Добавить
                            </button>
                            <button className="btn btn-secondary" onClick={() => setShowPopup(false)}>
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <table className="table table-bordered">
                <thead>
                <tr>
                    <th>VIN</th>
                    <th>Бренд</th>
                    <th>Модель</th>
                    <th>Год</th>
                    <th>Цена</th>
                    <th>Пробег</th>
                    <th>Статус</th>
                    <th>Создано</th>
                </tr>
                </thead>
                <tbody>
                {cars.length > 0 ? (
                    cars.map((car, index) => {
                        const details = carDetails[car.carId];
                        return (
                            <tr key={index}>
                                <td>{details ? details.vin : "Загрузка..."}</td>
                                <td>{details ? details.brand : "Загрузка..."}</td>
                                <td>{details ? details.model : "Загрузка..."}</td>
                                <td>{details ? details.year : "Загрузка..."}</td>
                                <td>{details ? `${formatPrice(details.price)} ₽` : "Загрузка..."}</td>
                                <td>{details ? `${details.mileage} км` : "Загрузка..."}</td>
                                <td>{car.status}</td>
                                <td>{car.createdAt ? formatDate(car.createdAt) : "Загрузка..."}</td>
                            </tr>
                        );
                    })
                ) : (
                    <tr>
                        <td colSpan="7">Машины не найдены</td>
                    </tr>
                )}
                </tbody>
            </table>

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
        </div>
    );
}

export default CarsPage;
