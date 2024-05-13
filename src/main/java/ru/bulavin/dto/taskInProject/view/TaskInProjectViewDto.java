package ru.bulavin.dto.taskInProject.view;

import lombok.Builder;

@Builder
public record TaskInProjectViewDto(String idTaskInProject,
                                   String idTask,
                                   String name,
                                   String description,
                                   String dateStart,
                                   String deadline,
                                   String status,
                                   String priority,
                                   String idUser,
                                   String idProject,
                                   String dateOfAddition) {
}
