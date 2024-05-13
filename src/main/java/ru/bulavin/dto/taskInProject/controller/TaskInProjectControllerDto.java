package ru.bulavin.dto.taskInProject.controller;

import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Project;
import ru.bulavin.entity.Status;
import ru.bulavin.entity.Task;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskInProjectControllerDto(Long idTaskInProject,
                                         Long idTask,
                                         String name,
                                         String description,
                                         LocalDateTime dateStart,
                                         LocalDateTime deadline,
                                         Status status,
                                         Priority priority,
                                         Long idUser,
                                         Long idProject,
                                         LocalDateTime dateOfAddition) {
}
