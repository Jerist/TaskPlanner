package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.processing.PasswordHashed;
import ru.bulavin.service.TaskService;
import ru.bulavin.service.UserService;

@UtilityClass
public class ServiceManager {
    @Getter
    private static final UserService userService;
    @Getter
    private static final TaskService taskService;

    static {
        userService = new UserService(MapperManager.getUserMapper(), new PasswordHashed(), DaoManager.getUserDao());
        taskService = new TaskService(DaoManager.getTaskDao(), MapperManager.getTaskMapper());
    }
}
