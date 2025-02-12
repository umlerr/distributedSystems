<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Результат</title>
    <!-- Подключение Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .result-container {
            max-width: 500px;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="result-container text-center">
        <h2 class="<%= (request.getAttribute("error") == null) ? "text-success" : "text-danger" %>">
            <%= request.getAttribute("message") %>
        </h2>

        <% if (request.getAttribute("error") == null) { %>
        <div class="text-start mt-3">
            <p><strong>Имя и фамилия:</strong> <%= request.getAttribute("name") %></p>
            <p><strong>Телефон:</strong> <%= request.getAttribute("phone") %></p>
            <p><strong>Номер машины:</strong> <%= request.getAttribute("carNumber") %></p>
            <p><strong>Модель:</strong> <%= request.getAttribute("model") %></p>
            <p><strong>Цена:</strong> <%= request.getAttribute("price") %> руб.</p>
        </div>
        <% } else { %>
        <p class="text-danger"><%= request.getAttribute("error") %></p>
        <% } %>

        <a href="autosale" class="btn btn-primary mt-3">Вернуться назад</a>
    </div>
</div>

<!-- Подключение Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
