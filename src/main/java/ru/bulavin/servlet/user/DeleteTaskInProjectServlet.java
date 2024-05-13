package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.service.TaskInProjectService;

import java.io.IOException;

import static ru.bulavin.utils.UrlPathGetter.*;

@WebServlet(DELETE_TASK_IN_PROJECT_URL)
public class DeleteTaskInProjectServlet extends HttpServlet {

    private TaskInProjectService taskInProjectService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.taskInProjectService = ServiceManager.getTaskInProjectService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idTask = Long.parseLong(req.getParameter("idTask"));
        Long idProject = Long.parseLong(req.getParameter("idProject"));
        taskInProjectService.delete(idTask, idProject);

        req.getSession().setAttribute ("message", "Задача успешно удалена");
        resp.sendRedirect(getFullPath(PROJECT_INFO_URL) + "?idProject=" + idProject);
    }

}
