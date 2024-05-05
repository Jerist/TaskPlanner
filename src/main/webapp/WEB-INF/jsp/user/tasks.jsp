<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            text-align: center;
            justify-content: center;
        }

        .empty-list {
            background-color: #fff;
            padding: 10px;
            border-radius: 5px;
            margin: 20px auto;
            width: 25%;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
        }

        form {
            background-color: #fff;
            padding: 2% 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin: 20px auto;
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


<c:choose>
    <c:when test="${not empty requestScope.tasksList}">



    </c:when>
    <c:otherwise>
        <div class="empty-list">
            Список задач пуст
        </div>
    </c:otherwise>
</c:choose>
