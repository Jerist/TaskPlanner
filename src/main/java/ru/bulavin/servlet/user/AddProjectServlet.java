package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.project.controller.ProjectControllerDto;
import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.service.ProjectService;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;

import static ru.bulavin.utils.AttributeGetter.NAME_ATTRIBUTE_USER;
import static ru.bulavin.utils.AttributeGetter.NAME_MESSAGE_ADD_PROJECT;
import static ru.bulavin.utils.JspPathGetter.ADD_PROJECT_JSP;
import static ru.bulavin.utils.JspPathGetter.LOGIN_JSP;
import static ru.bulavin.utils.UrlPathGetter.ADD_PROJECT_URL;

@WebServlet(ADD_PROJECT_URL)
public class AddProjectServlet extends HttpServlet {
    private static ProjectService projectService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        projectService = ServiceManager.getProjectService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getUserPath(ADD_PROJECT_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if(projectService.existsByName(name,
                ((UserControllerDto)req.getSession().getAttribute(NAME_ATTRIBUTE_USER)).idUser())) {
            req.setAttribute("add_project_error", "Проект с таким именем уже существует");
            req.getRequestDispatcher(JspPathCreator.getUserPath(ADD_PROJECT_JSP)).forward(req, resp);
        }
        String description = req.getParameter("description");
        ProjectControllerDto projectControllerDto = ProjectControllerDto.builder()
                .name(name)
                .description(description)
                .idUser(((UserControllerDto)req.getSession().getAttribute(NAME_ATTRIBUTE_USER)).idUser())
                .build();
        projectService.insertProject(projectControllerDto);
        req.setAttribute(NAME_MESSAGE_ADD_PROJECT, "Проект успешно добавлен");
        req.getRequestDispatcher(JspPathCreator.getUserPath(ADD_PROJECT_JSP)).forward(req, resp);
    }
}
