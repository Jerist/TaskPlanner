package ru.bulavin.service;

import ru.bulavin.dto.task.controller.TaskControllerDto;
import ru.bulavin.mapper.TaskMapper;
import ru.bulavin.repository.TaskDao;

import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskDao taskDao;
    private final TaskMapper taskMapper;

    public TaskService(TaskDao taskDao, TaskMapper taskMapper) {
        this.taskDao = taskDao;
        this.taskMapper = taskMapper;
    }

    public void insertTask() {

    }
    public List<TaskControllerDto> selectAllTasksByUserId(Long id) {
        return taskDao.selectByUserId(id).stream().map(taskMapper::map).collect(Collectors.toList());
    }
}
