package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.mapper.ProjectMapper;
import ru.bulavin.mapper.TaskInProjectMapper;
import ru.bulavin.mapper.TaskMapper;
import ru.bulavin.mapper.UserMapper;


@UtilityClass
public class MapperManager {
    @Getter
    private static final UserMapper userMapper;

    @Getter
    private static final TaskMapper taskMapper;

    @Getter
    private static final ProjectMapper projectMapper;

    @Getter
    private static final TaskInProjectMapper taskInProjectMapper;

    static {
        userMapper = new UserMapper();
        taskMapper = new TaskMapper();
        projectMapper = new ProjectMapper();
        taskInProjectMapper = new TaskInProjectMapper();
    }
}
