<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add Project</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #483D8B;
        }
        .form-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            margin-top: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            width: 40%;
        }
        form {
            display: flex;
            flex-direction: column;
            width: 100%;
        }
        label {
            margin: 10px 0 5px;
        }
        input[type="text"], input[type="submit"] {
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            background-color: #5A78E7;
            color: white;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #435BAE;
        }
        .error_message, .add_project_message {
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
<div class = forms>
<% if (request.getAttribute("add_project_error") != null) { %>
<div class="add_project_message">
    <%= request.getAttribute("add_project_error") %>
</div>
<% } %>
    <% if (request.getAttribute("task_add_project") != null) { %>
    <div class="error_message">
        <%= request.getAttribute("task_add_project") %>
    </div>
    <% } %>
<div class="form-container">
    <h1>Add New Project</h1>
    <form action="addProject" method="post">
        <label for="name">Project Name:</label>
        <input type="text" id="name" name="name" required>

        <label for="description">Description:</label>
        <input type="text" id="description" name="description">

        <input type="submit" value="Add Project">
    </form>
</div>
</div>
</body>
</html>
