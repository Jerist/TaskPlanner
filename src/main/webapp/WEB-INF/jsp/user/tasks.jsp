<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Task Manager</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #483D8B; /* Установка цвета фона */
        }
        .header {
            width: 100%;
            display: flex;
            justify-content: space-between;
            background-color: #f4f4f4;
            padding: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .task {
            margin: 20px;
            border: 1px solid #ddd;
            padding: 20px;
            width: 60%; /* Adjust the width as needed */
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            background: #fff;
            display: flex;
            flex-direction: column;
            align-items: center;
            border-radius: 10px; /* Закругление углов блока задач */
        }
        .task-info {
            text-align: center;
            width: 100%;
        }
        .task-actions {
            width: 100%;
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 10px;
        }
        form {
            margin: 0;
            border: 2px solid #888; /* Граница для форм */
            border-radius: 8px; /* Закругление углов форм */
            padding: 10px;
            background-color: #EEE; /* Фон форм */
            width: auto;
        }
        input[type="submit"] {
            border: none; /* Убраны границы у кнопок */
            padding: 8px 16px;
            border-radius: 5px; /* Закругление кнопок */
            cursor: pointer;
            background-color: #5A78E7;
            color: white;
            width: 100%;
            height: 30px;
        }
        input[type="submit"]:hover {
            background-color: #435BAE;
        }
        .logout {
            height: 30px;
        }
    </style>
</head>
<body>
<div class="header">

    <form action="logout" class="logout" method="get">
        <input type="submit" value="Logout">
    </form>
    <div>
        <form action="addTask" method="get">
            <input type="submit" value="Add New Task">
        </form>
        <form action="addProject" method="get">
            <input type="submit" value="Add New Project">
        </form>
        <form action="filterTasks" method="get">
            <input type="hidden" name="status" value="In Progress">
            <input type="submit" value="Filter Tasks: In Progress">
        </form>
        <form action="showAllTasks" method="get">
            <input type="submit" value="Show All Tasks">
        </form>
    </div>
</div>

<!-- Отображение каждой задачи в отдельной форме -->
<c:forEach var="task" items="${tasksList}">
    <div class="task">
        <div class="task-info">
            <h3>${task.name()}</h3>
            <p><strong>Description:</strong> ${task.description()}</p>
            <p><strong>Start Date:</strong> ${task.dateStart()}</p>
            <p><strong>Deadline:</strong> ${task.deadline()}</p>
            <p><strong>Status:</strong> ${task.status()}</p>
            <p><strong>Priority:</strong> ${task.priority()}</p>
        </div>
        <div class="task-actions">
            <form action="updateTask" method="post">
                <input type="hidden" name="idTask" value="${task.idTask()}">
                <input type="submit" value="Update Task">
            </form>
            <form action="deleteTask" method="post">
                <input type="hidden" name="idTask" value="${task.idTask()}">
                <input type="submit" value="Delete Task" style="background-color: red;">
            </form>
        </div>
    </div>
</c:forEach>
</body>
</html>
