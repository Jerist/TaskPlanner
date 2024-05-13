package ru.bulavin.dto.taskInProject.view;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AddTaskToProjectViewDto(String idTaskInProject,
                                      String idTask,
                                      String idProject,
                                      String dateOfAddition) {
}
