<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Project</title>
    <style>
        body {

            font-family: Arial, sans-serif;
            background-color: #483D8B;
        }
        .forms {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 0;
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
        }
        label {
            margin: 10px 0 5px;
        }
        input[type="text"], input[type="submit"], select {
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
        .project_update_message {
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
    </style>
</head>
<body>
<a class="back" href="<c:url value='/user/project?idProject=${project.idProject()}'/>">
    <button type="button">Back</button>
</a>
<div class = forms>
<c:if test="${not empty sessionScope.message}">
    <div class="project_update_message">
        <p>${sessionScope.message}</p>
    </div>
    <c:remove var="message" scope="session"/>
</c:if>
<div class="form-container">
    <h1>Edit Project</h1>
    <form action="updateProject" method="post">
        <input type="hidden" name="idProject" value="${project.idProject()}">

        <label for="name">Project Name:</label>
        <input type="text" placeholder="${project.name()}" id="name" name="name" value="${project.name()}">

        <input type="hidden" name="descriptionProject" value="${project.idProject()}">

        <label for="description">Project Description:</label>
        <input type="text" placeholder="${project.description()}" id="description" name="description" value="${project.description()}">

        <input type="submit" value="Update Project">
    </form>

    <h2>Add Existing Task</h2>
    <form action="addExistingTaskToProject" method="post">
        <input type="hidden" name="projectId" value="${project.idProject()}">

        <label for="task">Select Task:</label>
        <select id="task" name="taskId">
            <c:forEach items="${tasksList}" var="task">
                <option value="${task.idTask()}">${task.name()}</option>
            </c:forEach>
        </select>

        <input type="submit" value="Add Task">
    </form>
</div>
</div>
</body>
</html>
