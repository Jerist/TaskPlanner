<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            text-align: center;
            background-color: darkslateblue;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
        }

        .error-message {
            color: #D8000C;
            background-color: #FFD2D2;
            padding: 10px;
            border-radius: 5px;
            margin: 20px;
            width: 50%;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        form {
            background-color: #fff;
            padding: 2% 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin: 20px auto;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="tel"], input[type="password"], input[type="submit"] {
            padding: 8px;
            width: 100%;
            margin-bottom: 20px;
        }

        button {
            padding: 10px 20px;
            background-color: #5C7AEA;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            display: block;
            margin: 10px auto;
            width: 80%;
        }

        button:hover {
            background-color: #3f5bcc;
        }

        a {
            text-decoration: none;
        }
        div {
            margin: 20px 20px;
        }
    </style>
</head>
<body>

<c:if test="${not empty requestScope.error}">
    <div class="error-message">
        <span>${requestScope.error.toString()}</span>
    </div>
</c:if>

<form method="post" action="<c:url value='/login'/>">
    <h2>Введите данные для входа</h2>
    <div>
        <label for="phone">Телефон:</label>
        <input type="tel" placeholder="+*(***)***-**-**" id="phone" name="phone" required>
    </div>
    <div>
        <label for="password">Пароль:</label>
        <input type="password" placeholder="password" id="password" name="password" required>
    </div>
    <div>
        <input type="submit" value="Login">
    </div>
    <a href="<c:url value='/registration'/>">
        <button type="button">Register</button>
    </a>
</form>

</body>
</html>