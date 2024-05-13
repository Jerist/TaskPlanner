<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Project Details</title>
</head>
<style>
    body {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin: 0;
        font-family: Arial, sans-serif;
        background-color: #483D8B;
    }
    .header {
        width: 100%;
        display: flex;
        justify-content: space-between;
        background-color: #31276f;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .project, .task {
        margin: 20px;
        border: 1px solid #ddd;
        padding: 20px;
        width: 60%;
        box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        background: #fff;
        display: flex;
        flex-direction: column;
        align-items: center;
        border-radius: 10px;
    }
    .project-info, .task-info {
        text-align: center;
        width: 100%;
    }
    .project-actions, .task-actions {
        width: 100%;
        display: flex;
        justify-content: space-around;
        margin-top: 10px;
    }
    a, input[type="submit"] {
        padding: 10px;
        border-radius: 5px;
        text-decoration: none;
        color: white;
        background-color: #5A78E7;
        border: none;
        cursor: pointer;
    }
    a {
        height: 15px;
    }
    a:hover, input[type="submit"]:hover {
        background-color: #435BAE;
    }

    .message {
        margin-top: 10px;
        border: 2px solid #888;
        border-radius: 8px;
        padding: 10px;
        background-color: white;
    }
</style>
<body>
<div class="header">
    <a href="<c:url value='/user/tasks'/>">Home</a>
    <form action="logout" method="get">
        <input type="submit" value="Logout">
    </form>
</div>


<c:if test="${not empty sessionScope.message}">
    <div class="message">
            ${sessionScope.message}
    </div>
    <c:remove var="message" scope="session"/>
</c:if>

<div class="project">
    <div class="project-info">
        <h1>${project.name()}</h1>
        <p><strong>${project.description()}</strong></p>
        <div class="project-actions">
            <a href="updateProject?idProject=${project.idProject()}">Edit Project</a>
            <form action="deleteProject" method="post">
                <input type="hidden" name="idProject" value="${project.idProject()}">
                <input type="submit" value="Delete Project" style="background-color: red;">
            </form>
        </div>
    </div>
</div>

<c:forEach var="task" items="${projectTasks}">
    <div class="task">
        <div class="task-info">
            <h3>${task.name()}</h3>
            <p><strong>Description:</strong> ${task.description()}</p>
            <p><strong>Start Date:</strong> ${task.dateStart()}</p>
            <p><strong>Deadline:</strong> ${task.deadline()}</p>
            <p><strong>Status:</strong> ${task.status()}</p>
            <p><strong>Priority:</strong> ${task.priority()}</p>
            <p><strong>Date of addition:</strong> ${task.dateOfAddition()}</p>
        </div>
        <div class="task-actions">
            <a href="updateTask?idTask=${task.idTask()}">Edit Task</a>
            <form action="deleteTaskInProject" method="post">
                <input type="hidden" name="idTask" value="${task.idTask()}">
                <input type="hidden" name="idProject" value="${task.idProject()}">
                <input type="submit" value="Delete Task" style="background-color: red;">
            </form>
        </div>
    </div>
</c:forEach>
</body>
</html>
