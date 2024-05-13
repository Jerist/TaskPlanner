package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.project.controller.ProjectControllerDto;
import ru.bulavin.dto.project.view.ProjectViewDto;
import ru.bulavin.dto.task.controller.TaskControllerDto;
import ru.bulavin.dto.task.view.TaskViewDto;
import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.mapper.ProjectMapper;
import ru.bulavin.mapper.TaskInProjectMapper;
import ru.bulavin.mapper.TaskMapper;
import ru.bulavin.service.ProjectService;
import ru.bulavin.service.TaskInProjectService;
import ru.bulavin.service.TaskService;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.bulavin.utils.AttributeGetter.NAME_ATTRIBUTE_USER;
import static ru.bulavin.utils.JspPathGetter.UPDATE_PROJECT_JSP;
import static ru.bulavin.utils.JspPathGetter.UPDATE_TASK_JSP;
import static ru.bulavin.utils.UrlPathGetter.UPDATE_PROJECT_URL;
import static ru.bulavin.utils.UrlPathGetter.getFullPath;

@WebServlet(UPDATE_PROJECT_URL)
public class UpdateProjectServlet extends HttpServlet {
    private ProjectService projectService;
    private ProjectMapper projectMapper;
    private TaskService taskService;
    private TaskMapper taskMapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        projectService = ServiceManager.getProjectService();
        projectMapper = MapperManager.getProjectMapper();
        taskService = ServiceManager.getTaskService();
        taskMapper = MapperManager.getTaskMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idProject = Long.parseLong(req.getParameter("idProject"));
        ProjectViewDto project = projectMapper.mapController(projectService.selectById(idProject));

        List<TaskViewDto> tasks = taskService.selectAllTasksByUserId(
                ((UserControllerDto)req.getSession().getAttribute(NAME_ATTRIBUTE_USER)).idUser())
                .stream().map(taskMapper::mapController).toList();

        req.setAttribute("project", project);
        req.setAttribute("tasksList", tasks);
        req.getRequestDispatcher(JspPathCreator.getUserPath(UPDATE_PROJECT_JSP)).forward(req, resp);;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idProject = Long.parseLong(req.getParameter("idProject"));
        ProjectControllerDto oldTask = projectService.selectById(idProject);

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        ProjectControllerDto project = ProjectControllerDto.builder()
                .idProject(oldTask.idProject())
                .name(name.isEmpty()?oldTask.name():name)
                .description(description.isEmpty()?oldTask.description():name)
                .idUser(((UserControllerDto)req.getSession().getAttribute(NAME_ATTRIBUTE_USER)).idUser())
                .build();
        projectService.update(project);

        req.getSession().setAttribute("message", "Задача успешно добавлена в проект");
        resp.sendRedirect(getFullPath(UPDATE_PROJECT_URL) + "?idProject=" + idProject);
    }
}
