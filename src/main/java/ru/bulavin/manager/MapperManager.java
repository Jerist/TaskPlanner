package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.mapper.TaskMapper;
import ru.bulavin.mapper.UserMapper;


@UtilityClass
public class MapperManager {
    @Getter
    private static final UserMapper userMapper;

    @Getter
    private static final TaskMapper taskMapper;

    static {
        userMapper = new UserMapper();
        taskMapper = new TaskMapper();
    }
}
