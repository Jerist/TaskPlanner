package ru.bulavin.dto.taskInProject.controller;

import lombok.Builder;
import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;

import java.time.LocalDateTime;

@Builder
public record AddTaskToProjectControllerDto(Long idTaskInProject,
                                            Long idTask,
                                            Long idProject,
                                            LocalDateTime dateOfAddition) {
}
