package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.taskInProject.controller.AddTaskToProjectControllerDto;
import ru.bulavin.dto.taskInProject.view.AddTaskToProjectViewDto;
import ru.bulavin.entity.TaskInProject;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.mapper.TaskInProjectMapper;
import ru.bulavin.service.TaskInProjectService;

import java.io.IOException;

import static ru.bulavin.utils.AttributeGetter.NAME_MESSAGE_ADD_PROJECT;
import static ru.bulavin.utils.UrlPathGetter.*;

@WebServlet(ADD_EXIST_TASK_TO_PROJECT_URL)
public class AddExistingTaskToProjectServlet extends HttpServlet {

    private TaskInProjectService taskInProjectService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.taskInProjectService = ServiceManager.getTaskInProjectService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idTask = Long.parseLong(req.getParameter("taskId"));
        Long idProject = Long.parseLong(req.getParameter("projectId"));
        AddTaskToProjectControllerDto task = AddTaskToProjectControllerDto.builder()
                .idProject(idProject)
                .idTask(idTask)
                .build();
        taskInProjectService.insert(task);
        req.getSession().setAttribute("message", "Задача успешно добавлена в проект");
        resp.sendRedirect(getFullPath(UPDATE_PROJECT_URL) + "?idProject=" + idProject);
    }
}
