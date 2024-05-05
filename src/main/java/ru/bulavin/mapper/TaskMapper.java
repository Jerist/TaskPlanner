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
                .name(obj.getName())
                .description(obj.getDescription())
                .dateStart(obj.getDateStart())
                .deadline(obj.getDeadline())
                .status(obj.getStatus())
                .priority(obj.getPriority())
                .build();
    }
    public TaskViewDto map(TaskControllerDto obj) {
        return TaskViewDto.builder()
                .name(obj.name())
                .description(obj.description())
                .dateStart(obj.dateStart().toString())
                .deadline(obj.deadline().toString())
                .status(obj.status().toString())
                .priority(obj.priority().toString())
                .build();
    }
}
