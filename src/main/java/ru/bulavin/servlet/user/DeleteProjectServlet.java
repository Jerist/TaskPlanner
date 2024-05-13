package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.service.ProjectService;

import java.io.IOException;

import static ru.bulavin.utils.UrlPathGetter.*;

@WebServlet(DELETE_PROJECT_URL)
public class DeleteProjectServlet extends HttpServlet {

    private ProjectService projectService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.projectService = ServiceManager.getProjectService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idProject = Long.parseLong(req.getParameter("idProject"));
        projectService.delete(idProject);

        req.getSession().setAttribute ("message", "Проект успешно удалена");
        resp.sendRedirect(getFullPath(TASKS_LIST_URL));
    }
}
