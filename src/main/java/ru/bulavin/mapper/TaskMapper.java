package ru.bulavin.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.bulavin.dto.task.controller.TaskControllerDto;
import ru.bulavin.dto.task.view.TaskViewDto;
import ru.bulavin.entity.Task;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TaskMapper {
    public TaskControllerDto map(Task obj) {
        return TaskControllerDto.builder()
                .idTask(obj.getIdTask())
                .name(obj.getName())
                .description(obj.getDescription())
                .dateStart(obj.getDateStart())
                .deadline(obj.getDeadline())
                .status(obj.getStatus())
                .priority(obj.getPriority())
                .build();
    }

    public Task map(TaskControllerDto obj) {
        return Task.builder()
                .idTask(obj.idTask())
                .name(obj.name())
                .description(obj.description())
                .dateStart(obj.dateStart())
                .deadline(obj.deadline())
                .status(obj.status())
                .priority(obj.priority())
                .idUser(obj.idUser())
                .build();
    }

    public TaskViewDto mapController(TaskControllerDto obj) {
        return TaskViewDto.builder()
                .name(obj.name())
                .description(obj.description().isEmpty()?"-":obj.description())
                .dateStart(obj.dateStart().toString())
                .deadline(obj.deadline()==null?"-":obj.deadline().toString())
                .status(obj.status().toString())
                .priority(obj.priority().toString())
                .build();
    }
}
