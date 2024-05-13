package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.project.view.ProjectViewDto;
import ru.bulavin.dto.task.view.TaskViewDto;
import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.mapper.ProjectMapper;
import ru.bulavin.mapper.TaskMapper;
import ru.bulavin.service.ProjectService;
import ru.bulavin.service.TaskService;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;
import java.util.List;

import static ru.bulavin.utils.AttributeGetter.NAME_ATTRIBUTE_USER;
import static ru.bulavin.utils.JspPathGetter.TASKS_LIST_JSP;
import static ru.bulavin.utils.UrlPathGetter.TASKS_LIST_URL;

@WebServlet(TASKS_LIST_URL)
public class TasksListServlet extends HttpServlet {
    private TaskService taskService;
    private TaskMapper taskMapper;
    private ProjectService projectService;
    private ProjectMapper projectMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final UserControllerDto user = (UserControllerDto) req.getSession().getAttribute(NAME_ATTRIBUTE_USER);
        final List<TaskViewDto> tasksListDto = getTasksList(req.getParameter("status"), user);
        final List<ProjectViewDto> projectsList = projectService.selectByUserId(user.idUser()).stream()
                        .map(projectMapper::mapController).toList();
        req.setAttribute("projectsList", projectsList);
        req.setAttribute("tasksList", tasksListDto);
        req.getRequestDispatcher(JspPathCreator.getUserPath(TASKS_LIST_JSP)).forward(req, resp);
    }

    private List<TaskViewDto> getTasksList(String status, UserControllerDto user) {
        return switch (status) {
            case "All tasks" -> taskService.selectAllTasksByUserId(user.idUser()).stream()
                    .map(taskMapper::mapController)
                    .toList();
            case "Active" -> defaultTasksList(status, user);
            case null, default -> defaultTasksList(status, user);
        };
    }
    private List<TaskViewDto> defaultTasksList(String status, UserControllerDto user) {
        return taskService.selectAllActiveTasksByUserId(user.idUser()).stream()
                .map(taskMapper::mapController)
                .toList();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        taskService = ServiceManager.getTaskService();
        taskMapper = MapperManager.getTaskMapper();
        projectService = ServiceManager.getProjectService();
        projectMapper = MapperManager.getProjectMapper();
    }
}
