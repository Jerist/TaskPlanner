package ru.bulavin.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.task.controller.TaskControllerDto;
import ru.bulavin.dto.task.view.TaskViewDto;
import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.mapper.TaskMapper;
import ru.bulavin.service.TaskService;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDateTime;

import static ru.bulavin.utils.AttributeGetter.NAME_ATTRIBUTE_USER;
import static ru.bulavin.utils.JspPathGetter.UPDATE_TASK_JSP;
import static ru.bulavin.utils.UrlPathGetter.UPDATE_TASK_URL;

@WebServlet(UPDATE_TASK_URL)
public class UpdateTaskServlet extends HttpServlet {
    private TaskService taskService;
    private TaskMapper taskMapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.taskService = ServiceManager.getTaskService();
        this.taskMapper = MapperManager.getTaskMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long taskId = Long.parseLong(req.getParameter("idTask"));
        TaskViewDto task = taskMapper.mapController(taskService.selectById(taskId));
        req.setAttribute("task", task);
        req.getRequestDispatcher(JspPathCreator.getUserPath(UPDATE_TASK_JSP)).forward(req, resp);;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long taskId = Long.parseLong(req.getParameter("idTask"));
        TaskControllerDto oldTask = taskService.selectById(taskId);

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String dateStartStr = req.getParameter("dateStart");
        LocalDateTime startDate = dateStartStr.isEmpty()?oldTask.dateStart():LocalDateTime.parse(dateStartStr);

        String deadlineStr = req.getParameter("deadline");
        LocalDateTime deadline = deadlineStr.isEmpty()?oldTask.deadline():LocalDateTime.parse(deadlineStr);

        Status status = Status.valueOf(req.getParameter("status"));
        Priority priority = Priority.valueOf(req.getParameter("priority"));
        TaskControllerDto task = TaskControllerDto.builder()
                .idTask(oldTask.idTask())
                .name(name.isEmpty()?oldTask.name():name)
                .description(description.isEmpty()? oldTask.description():description)
                .dateStart(startDate)
                .deadline(deadline)
                .status(status)
                .priority(priority)
                .idUser(((UserControllerDto)req.getSession().getAttribute(NAME_ATTRIBUTE_USER)).idUser())
                .build();
        taskService.update(task);
        req.getSession().setAttribute("message", "Задача успешно изменена");
        req.getRequestDispatcher(JspPathCreator.getUserPath(UPDATE_TASK_JSP)).forward(req, resp);
    }
}
