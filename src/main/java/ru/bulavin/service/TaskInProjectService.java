package ru.bulavin.service;

import ru.bulavin.dto.taskInProject.controller.AddTaskToProjectControllerDto;
import ru.bulavin.dto.taskInProject.controller.TaskInProjectControllerDto;
import ru.bulavin.mapper.TaskInProjectMapper;
import ru.bulavin.repository.TaskInProjectDao;

import java.util.List;

public class TaskInProjectService {
    private final TaskInProjectDao taskInProjectDao;
    private final TaskInProjectMapper taskInProjectMapper;

    public TaskInProjectService(TaskInProjectDao taskInProjectDao, TaskInProjectMapper taskInProjectMapper) {
        this.taskInProjectDao = taskInProjectDao;
        this.taskInProjectMapper = taskInProjectMapper;
    }

    public List<TaskInProjectControllerDto> selectAllTaskByProjectId(Long id) {
        return taskInProjectDao.selectByProjectId(id).stream().map(taskInProjectMapper::map).toList();
    }

    public void delete(Long idTask, Long idProject) {
        taskInProjectDao.delete(taskInProjectDao.selectByTaskAndProject(idTask, idProject).getIdTaskInProject());
    }
    public void insert(AddTaskToProjectControllerDto task) {
        taskInProjectDao.insert(taskInProjectMapper.map(task));
    }
}
