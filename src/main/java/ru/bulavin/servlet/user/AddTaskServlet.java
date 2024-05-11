package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.task.controller.TaskControllerDto;
import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.service.TaskService;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDateTime;

import static ru.bulavin.utils.AttributeGetter.*;
import static ru.bulavin.utils.JspPathGetter.ADD_TASK_JSP;
import static ru.bulavin.utils.UrlPathGetter.ADD_TASK_URL;

@WebServlet(ADD_TASK_URL)
public class AddTaskServlet extends HttpServlet {

    private static TaskService taskService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        taskService = ServiceManager.getTaskService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getUserPath(ADD_TASK_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        LocalDateTime startDate = LocalDateTime.parse(req.getParameter("dateStart"));
        String dateDeadline = req.getParameter("deadline");
        LocalDateTime deadline = dateDeadline.isEmpty() ? null: LocalDateTime.parse(dateDeadline);
        Status status = Status.InProcess;
        Priority priority = Priority.valueOf(req.getParameter("priority"));
        TaskControllerDto taskControllerDto = TaskControllerDto.builder()
                .name(name)
                .description(description)
                .dateStart(startDate)
                .deadline(deadline)
                .status(status)
                .priority(priority)
                .idUser(((UserControllerDto)req.getSession().getAttribute(NAME_ATTRIBUTE_USER)).idUser())
                .build();

        taskService.insertTask(taskControllerDto);
        req.setAttribute(NAME_MESSAGE_ADD_TASK, "Задача успешно добавлена");
        doGet(req, resp);
    }
}
