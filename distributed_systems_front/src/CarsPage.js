import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function CarsPage() {
    const [cars, setCars] = useState([]);
    const [carDetails, setCarDetails] = useState({});
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
                navigate("/"); // Redirect to login if there's no token
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
            carsData.content.forEach((car) => fetchUserDetails(car.userId));
        } catch (error) {
            console.error("Ошибка при получении списка машин:", error);
        }
    };

    const fetchCarDetails = async (carId) => {
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                navigate("/"); // Redirect to login if there's no token
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

    const fetchUserDetails = async (userId) => {
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                navigate("/");
                return;
            }

            const response = await fetch(
                `http://localhost:8080/api/auth/v1/user-by-id/${userId}`,
                {
                    method: "GET",
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            if (!response.ok) {
                throw new Error(await response.text());
            }

            const carData = await response.json();
            setCarDetails((prevDetails) => ({ ...prevDetails, [userId]: carData }));
        } catch (error) {
            console.error(`Ошибка при получении данных пользователя ${userId}:`, error);
        }
    };

    const handleAddCar = async (e) => {
        e.preventDefault(); // Prevent the form from submitting and refreshing the page
        setErrorMessage(""); // Clear any previous error messages
        try {
            const token = localStorage.getItem("authToken");
            if (!token) {
                navigate("/"); // Redirect to login if there's no token
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
                if (response.status === 409) {
                    // If VIN already exists (409 Conflict), display the specific error
                    setErrorMessage("Ошибка: Машина с таким VIN номером уже существует.");
                } else {
                    // For other errors, display a general error message
                    const errorText = await response.text();
                    throw new Error(errorText);
                }
            } else {
                // If the car was successfully added, reset the form and close the popup
                setShowPopup(false);
                setNewCar({ vin: "", brand: "", model: "", price: "", year: "", mileage: "" });
                fetchCars(currentPage, pageSize); // Refresh the list of cars
            }
        } catch (error) {
            // Catch any other errors and display a generic error message
            setErrorMessage("Ошибка при добавлении объявления. Попробуйте снова.");
            console.error("Ошибка:", error);
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
                        <form onSubmit={handleAddCar}>
                            <input
                                type="text"
                                placeholder="VIN"
                                value={newCar.vin}
                                onChange={(e) => setNewCar({ ...newCar, vin: e.target.value })}
                                pattern="^[A-HJ-NPR-Z0-9]{17}$"
                                title="VIN номер должен содержать 17 символов, исключая I, O, Q"
                                required
                            />
                            <input
                                type="text"
                                placeholder="Марка"
                                value={newCar.brand}
                                onChange={(e) => setNewCar({ ...newCar, brand: e.target.value })}
                                pattern="^[A-Za-z\s]{1,20}$"
                                title="Бренд не должен содержать цифры и должен быть не более 20 символов"
                                required
                            />
                            <input
                                type="text"
                                placeholder="Модель"
                                value={newCar.model}
                                onChange={(e) => setNewCar({ ...newCar, model: e.target.value })}
                                pattern="^[A-Za-z0-9\s]{1,20}$"
                                title="Модель должна быть не более 20 символов и может содержать цифры"
                                required
                            />
                            <input
                                type="number"
                                placeholder="Цена"
                                value={newCar.price}
                                onChange={(e) => setNewCar({ ...newCar, price: e.target.value })}
                                pattern="^[0-9]{1,20}$"
                                title="Цена должна быть числом и не более 20 символов"
                                required
                            />
                            <input
                                type="number"
                                placeholder="Год"
                                value={newCar.year}
                                onChange={(e) => setNewCar({ ...newCar, year: e.target.value })}
                                min="1885"
                                max="2025"
                                title="Год выпуска не может быть раньше 1885 и не позже 2025"
                                required
                            />
                            <input
                                type="number"
                                placeholder="Пробег"
                                value={newCar.mileage}
                                onChange={(e) => setNewCar({ ...newCar, mileage: e.target.value })}
                                pattern="^[0-9]{1,8}$"
                                title="Пробег должен быть числом и не более 8 цифр"
                                required
                            />
                            {/* Display error message if there's any */}
                            {errorMessage && <div className="error-message">{errorMessage}</div>}
                            <div className="popup-buttons">
                                <button className="btn btn-success" type="submit">
                                    Добавить
                                </button>
                                <button
                                    className="btn btn-secondary"
                                    type="button"
                                    onClick={() => setShowPopup(false)}
                                >
                                    Отмена
                                </button>
                            </div>
                        </form>
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
                    <th>Имя владельца</th>
                    <th>Телефон</th>
                    <th>Статус</th>
                    <th>Создано</th>
                </tr>
                </thead>
                <tbody>
                {cars.length > 0 ? (
                    cars.map((car, index) => {
                        const details = carDetails[car.carId];
                        const userDetails = carDetails[car.userId];
                        return (
                            <tr key={index}>
                                <td>{details ? details.vin : "Загрузка..."}</td>
                                <td>{details ? details.brand : "Загрузка..."}</td>
                                <td>{details ? details.model : "Загрузка..."}</td>
                                <td>{details ? details.year : "Загрузка..."}</td>
                                <td>{details ? `${formatPrice(details.price)} ₽` : "Загрузка..."}</td>
                                <td>{details ? `${details.mileage} км` : "Загрузка..."}</td>
                                <td>{userDetails ? userDetails.name : "Загрузка..."}</td>
                                <td>{userDetails ? userDetails.phone : "Загрузка..."}</td>
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
