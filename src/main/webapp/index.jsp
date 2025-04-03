<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 31.03.2025
  Time: 9:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EMS Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body>
<nav class="flex justify-between items-center p-4 bg-white shadow-md">
    <div class="text-xl font-bold text-blue-600">EMS</div>
    <div class="space-x-4">
        <a href="#" class="text-blue-600">Home</a>
        <a href="#features" class="text-blue-600">Features</a>
        <a href="#about" class="text-blue-600">About</a>
        <a href="login.jsp" class="border border-blue-600 text-blue-600 px-4 py-2 rounded">Login</a>
        <a href="employeeRegister.jsp" class="bg-blue-600 text-white px-4 py-2 rounded">Sign Up</a>
    </div>
</nav>

<section class="text-center py-16 bg-gradient-to-r from-blue-500 to-blue-700 text-white">
    <h1 class="text-4xl font-semibold">Упростите управление персоналом с EMS</h1>
    <p class="mt-4 text-lg">Все инструменты HR в одном месте — от найма до аналитики</p>
    <div class="mt-6 space-x-4">
        <a href="signup.jsp" class="bg-white text-blue-600 px-6 py-2 rounded">Начать бесплатно</a>
        <a href="#" class="border border-white px-6 py-2 rounded">Демо-доступ</a>
    </div>
</section>

<section id="features" class="py-12 text-center">
    <h2 class="text-3xl font-semibold">Ключевые возможности</h2>
    <div class="mt-8 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="p-6 border rounded">
            <div class="text-4xl">👥</div>
            <h3 class="mt-4 font-semibold">Управление сотрудниками</h3>
            <p class="mt-2 text-gray-600">Регистрация, профили, ролевая модель</p>
        </div>
        <div class="p-6 border rounded">
            <div class="text-4xl">📊</div>
            <h3 class="mt-4 font-semibold">Аналитика</h3>
            <p class="mt-2 text-gray-600">Дашборды, отчеты в 1 клик</p>
        </div>
        <div class="p-6 border rounded">
            <div class="text-4xl">🔐</div>
            <h3 class="mt-4 font-semibold">Безопасность</h3>
            <p class="mt-2 text-gray-600">Шифрование данных, контроль доступа</p>
        </div>
    </div>
</section>

<section class="text-center py-12 bg-blue-600 text-white">
    <h2 class="text-2xl font-semibold">Готовы оптимизировать HR-процессы?</h2>
    <div class="mt-6 space-x-4">
        <a href="employeeRegister.jsp" class="bg-white text-blue-600 px-6 py-2 rounded">Зарегистрироваться</a>
        <a href="login.jsp" class="border border-white px-6 py-2 rounded">Войти</a>
    </div>
</section>

<footer class="text-center py-6 text-gray-600">
    <p>Контакты: email | <a href="#">Документация</a></p>
    <div class="mt-2">© 2024 EMS</div>
</footer>
</body>
</html>
