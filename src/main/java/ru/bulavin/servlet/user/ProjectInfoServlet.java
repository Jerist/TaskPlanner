package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.project.view.ProjectViewDto;
import ru.bulavin.dto.taskInProject.view.TaskInProjectViewDto;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.mapper.ProjectMapper;
import ru.bulavin.mapper.TaskInProjectMapper;
import ru.bulavin.service.ProjectService;
import ru.bulavin.service.TaskInProjectService;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;
import java.util.List;

import static ru.bulavin.utils.JspPathGetter.PROJECT_INFO_JSP;
import static ru.bulavin.utils.UrlPathGetter.PROJECT_INFO_URL;

@WebServlet(PROJECT_INFO_URL)
public class ProjectInfoServlet extends HttpServlet {
    private TaskInProjectService taskInProjectService;
    private TaskInProjectMapper taskInProjectMapper;
    private ProjectService projectService;
    private ProjectMapper projectMapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.taskInProjectService = ServiceManager.getTaskInProjectService();
        this.taskInProjectMapper = MapperManager.getTaskInProjectMapper();
        this.projectService = ServiceManager.getProjectService();
        this.projectMapper = MapperManager.getProjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idProject = Long.parseLong(req.getParameter("idProject"));
        List<TaskInProjectViewDto> tasksList = taskInProjectService.selectAllTaskByProjectId(idProject).stream()
                .map(taskInProjectMapper::map).toList();
        ProjectViewDto project = projectMapper.mapController(projectService.selectById(idProject));
        req.setAttribute("projectTasks", tasksList);
        req.setAttribute("project", project);
        req.getRequestDispatcher(JspPathCreator.getUserPath(PROJECT_INFO_JSP)).forward(req, resp);
    }
}
