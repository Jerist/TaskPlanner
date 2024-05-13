package ru.bulavin.mapper;


import ru.bulavin.dto.taskInProject.controller.AddTaskToProjectControllerDto;
import ru.bulavin.dto.taskInProject.controller.TaskInProjectControllerDto;
import ru.bulavin.dto.taskInProject.view.AddTaskToProjectViewDto;
import ru.bulavin.dto.taskInProject.view.TaskInProjectViewDto;
import ru.bulavin.entity.TaskInProject;

public class TaskInProjectMapper {
    public TaskInProjectControllerDto map(TaskInProject obj) {
        return TaskInProjectControllerDto.builder()
                .idTaskInProject(obj.getIdTaskInProject())
                .dateOfAddition(obj.getDateOfAddition())
                .idTask(obj.getIdTask())
                .name(obj.getName())
                .description(obj.getDescription())
                .dateStart(obj.getDateStart())
                .deadline(obj.getDeadline())
                .status(obj.getStatus())
                .priority(obj.getPriority())
                .idProject(obj.getIdProject())
                .build();
    }
    public TaskInProjectViewDto map(TaskInProjectControllerDto obj) {
        return TaskInProjectViewDto.builder()
                .idTaskInProject(obj.idTaskInProject().toString())
                .dateOfAddition(obj.dateOfAddition().toString())
                .idTask(obj.idTask().toString())
                .name(obj.name())
                .description(obj.description().isEmpty()?"-":obj.description())
                .dateStart(obj.dateStart().toString())
                .deadline(obj.deadline()==null?"-":obj.deadline().toString())
                .status(obj.status().toString())
                .priority(obj.priority().toString())
                .idProject(obj.idProject().toString())
                .build();
    }
    public AddTaskToProjectControllerDto map(AddTaskToProjectViewDto obj) {
        return AddTaskToProjectControllerDto.builder()
                .idProject(Long.parseLong(obj.idProject()))
                .idTask(Long.parseLong(obj.idTask()))
                .build();
    }
    public TaskInProject map(AddTaskToProjectControllerDto obj) {
        return TaskInProject.builder()
                .idProject(obj.idProject())
                .idTask(obj.idTask())
                .build();
    }
}
