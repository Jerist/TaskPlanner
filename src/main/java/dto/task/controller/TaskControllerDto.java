package dto.task.controller;

import entity.Priority;
import entity.Status;

import java.time.LocalDateTime;

public record TaskControllerDto(Long idTask,
                                String name,
                                String description,
                                LocalDateTime dateStart,
                                LocalDateTime deadline,
                                Status status,
                                Priority priority) {
}
