package ru.bulavin.service;

import ru.bulavin.dto.task.controller.TaskControllerDto;
import ru.bulavin.entity.Task;
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

    public void insertTask(TaskControllerDto taskControllerDto) {
        Task task = taskMapper.map(taskControllerDto);
        taskDao.insert(task);
    }
    public List<TaskControllerDto> selectAllTasksByUserId(Long id) {
        return taskDao.selectByUserId(id).stream().map(taskMapper::map).collect(Collectors.toList());
    }
    public List<TaskControllerDto> selectAllActiveTasksByUserId(Long id) {
        return taskDao.selectActiveTasksByUserId(id).stream().map(taskMapper::map).collect(Collectors.toList());
    }
    public TaskControllerDto selectById(Long id) {
        return taskMapper.map(taskDao.selectById(id));
    }
    public void deleteTask(Long id) {
        taskDao.delete(id);
    }
    public void update(TaskControllerDto task) {
        taskDao.update(taskMapper.map(task));
    }
}
