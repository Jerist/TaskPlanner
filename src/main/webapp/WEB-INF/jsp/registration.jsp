<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: darkslateblue;
            margin: 0;
            padding: 20px;
            display: flex;
            align-items: center;
            #text-align: center;
            justify-content: center;
            flex-direction: column;
        }

        .error-message {
            color: #D8000C;
            background-color: #FFD2D2;
            padding: 10px;
            border-radius: 5px;
            margin: 20px auto;
            width: 50%;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
        }

        form {
            background-color: #fff;
            padding: 2% 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin: 100px auto;
            width: 16%;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"], input[type="password"], input[type="tel"], input[type="email"], input[type="submit"] {
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
            margin: 10px 20px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
    </style>
</head>
<body>

<c:if test="${not empty requestScope.errors}">
    <div class="error-message">
        <c:forEach var="error" items="${requestScope.errors}">
            <span>${error.field}: </span><span>${error.type.toString()}</span><br/>
        </c:forEach>
    </div>
</c:if>

<form method="post" action="<c:url value='/registration'/>">
    <h2>Регистрация</h2>
    <div>
        <label for="name">Имя:</label>
        <input type="text" placeholder="nctest" id="name" name="name" required>
    </div>
    <div>
        <label for="phone">Телефон:</label>
        <input type="tel" placeholder="+*(***)***-**-**" id="phone" name="phone" required>
    </div>
    <div>
        <label for="email">Email:</label>
        <input type="email" placeholder="nctest@nctest.info" id="email" name="email" required>
    </div>
    <div>
        <label for="password">Пароль:</label>
        <input type="password" placeholder="password" id="password" name="password" required>
    </div>
    <div>
        <input type="submit" value="Register">
    </div>
    <a href="<c:url value='/login'/>">
        <button type="button">Login</button>
    </a>
</form>

</body>
</html>