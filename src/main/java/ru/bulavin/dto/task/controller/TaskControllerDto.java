package ru.bulavin.dto.task.controller;

import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskControllerDto(Long idTask,
                                String name,
                                String description,
                                LocalDateTime dateStart,
                                LocalDateTime deadline,
                                Status status,
                                Priority priority,
                                Long idUser) {
}
