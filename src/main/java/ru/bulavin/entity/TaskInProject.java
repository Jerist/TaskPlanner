package ru.bulavin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TaskInProject {
    private Long idTaskInProject;
    private Task task;
    private Project project;
    private LocalDateTime dateOfAddition;
}
