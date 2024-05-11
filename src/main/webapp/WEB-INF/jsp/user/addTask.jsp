<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Add New Task</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #483D8B;
        }
        .form-container {
            margin-top: 40px;
            background-color: #fff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 300px;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin-bottom: 5px;
        }
        input, select {
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            background-color: #5A78E7;
            color: white;
            cursor: pointer;
            border: none;
        }
        input[type="submit"]:hover {
            background-color: #435BAE;
        }
        .task_add_message {
            margin-top: 35px;
            background-color: white;
            border: 1px solid #ccc;
            border-radius: 4px;
            padding: 8px;

        }
        .back {
            background-color: #5A78E7;
            color: white;
            cursor: pointer;
            padding: 8px;
            margin-top: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-left: 10px;
        }
        .back:hover {
            background-color: #435BAE;
        }
        .forms {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 0;

        }
    </style>
</head>
<body>
<a class="back" href="<c:url value='/user/tasks'/>">
    <button type="button">Back</button>
</a>
<div class="forms">
<c:if test="${requestScope.task_add_message!=null}">
    <div class="task_add_message">
        <p>Задача успешно добавлена!</p>
    </div>
</c:if>

<div class="form-container">
    <h2>Add New Task</h2>
    <form action="addTask" method="post">
        <label for="name">Task Name:</label>
        <input type="text" id="name" name="name" required>

        <label for="description">Description:</label>
        <textarea id="description" name="description"></textarea>

        <label for="dateStart">Start Date:</label>
        <input type="datetime-local" id="dateStart" name="dateStart" required>

        <label for="deadline">Deadline:</label>
        <input type="datetime-local" id="deadline" name="deadline">

        <label for="priority">Priority:</label>
        <select id="priority" name="priority">
            <option value="Low">Low</option>
            <option value="Medium">Medium</option>
            <option value="High">High</option>
        </select>

        <input type="submit" value="Add Task">
    </form>
</div>
</div>
</body>
</html>
