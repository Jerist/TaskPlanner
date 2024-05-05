package ru.bulavin.dto.taskInProject.controller;

import ru.bulavin.entity.Project;
import ru.bulavin.entity.Task;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskInProjectControllerDto(Long idTaskInProject,
                                         Task task,
                                         Project project,
                                         LocalDateTime dateOfAddition) {
}
