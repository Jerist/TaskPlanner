package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.repository.*;


@UtilityClass
public class DaoManager {
    @Getter
    private static final UserDao userDao;

    @Getter
    private static final UserExistsDao userExistsDao;

    @Getter
    private static final TaskDao taskDao;

    @Getter
    private static final ProjectDao projectDao;

    @Getter
    private static final TaskInProjectDao taskInProjectDao;

    static {
        userDao = new UserDao(ConnectionManager.getConnectionGetter());
        userExistsDao = new UserExistsDao(ConnectionManager.getConnectionGetter());
        taskDao = new TaskDao(ConnectionManager.getConnectionGetter());
        projectDao = new ProjectDao(ConnectionManager.getConnectionGetter(), userDao);
        taskInProjectDao = new TaskInProjectDao(ConnectionManager.getConnectionGetter());
    }
}
