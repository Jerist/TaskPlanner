package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.repository.TaskDao;
import ru.bulavin.repository.UserDao;
import ru.bulavin.repository.UserExistsDao;


@UtilityClass
public class DaoManager {
    @Getter
    private static final UserDao userDao;

    @Getter
    private static final UserExistsDao userExistsDao;

    @Getter
    private static final TaskDao taskDao;

    static {
        userDao = new UserDao(ConnectionManager.getConnectionGetter());
        userExistsDao = new UserExistsDao(ConnectionManager.getConnectionGetter());
        taskDao = new TaskDao(ConnectionManager.getConnectionGetter(), userDao);
    }
}
