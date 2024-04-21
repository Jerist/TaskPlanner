package dto.taskInProject.controller;

import entity.Project;
import entity.Task;

import java.time.LocalDateTime;

public record TaskInProjectControllerDto(Long idTaskInProject,
                                         Task task,
                                         Project project,
                                         LocalDateTime dateOfAddition) {
}
