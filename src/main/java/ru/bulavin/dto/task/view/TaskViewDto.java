package ru.bulavin.dto.task.view;

import lombok.Builder;

@Builder
public record TaskViewDto(String idTask,
                                String name,
                                String description,
                                String dateStart,
                                String deadline,
                                String status,
                                String priority,
                                String idUser) {
}
