package ru.bulavin.dto.taskInProject.view;

import lombok.Builder;

@Builder
public record TaskInProjectViewDto(String idTaskInProject,
                                   String task,
                                   String project,
                                   String dateOfAddition) {
}
