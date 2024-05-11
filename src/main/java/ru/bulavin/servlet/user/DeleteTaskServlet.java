package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.service.TaskService;

import java.io.IOException;

import static ru.bulavin.utils.UrlPathGetter.*;

@WebServlet(DELETE_TASK_URL)
public class DeleteTaskServlet extends HttpServlet {

    private static TaskService taskService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        taskService = ServiceManager.getTaskService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idTask = Long.parseLong(req.getParameter("idTask"));
        taskService.deleteTask(idTask);
        req.getSession().setAttribute ("message", "Задача успешно удалена");
        resp.sendRedirect(getFullPath(TASKS_LIST_URL));
    }
}
