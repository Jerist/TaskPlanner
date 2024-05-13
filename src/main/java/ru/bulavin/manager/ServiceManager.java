package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.processing.PasswordHashed;
import ru.bulavin.service.ProjectService;
import ru.bulavin.service.TaskInProjectService;
import ru.bulavin.service.TaskService;
import ru.bulavin.service.UserService;

@UtilityClass
public class ServiceManager {
    @Getter
    private static final UserService userService;
    @Getter
    private static final TaskService taskService;
    @Getter
    private static final ProjectService projectService;
    @Getter
    private static final TaskInProjectService taskInProjectService;

    static {
        userService = new UserService(MapperManager.getUserMapper(), new PasswordHashed(), DaoManager.getUserDao());
        taskService = new TaskService(DaoManager.getTaskDao(), MapperManager.getTaskMapper());
        projectService = new ProjectService(DaoManager.getProjectDao(), MapperManager.getProjectMapper());
        taskInProjectService = new TaskInProjectService(DaoManager.getTaskInProjectDao(), MapperManager.getTaskInProjectMapper());
    }
}
