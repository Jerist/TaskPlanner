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
    private Long idTask;
    private String name;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime deadline;
    private Status status;
    private Priority priority;
    private Long idUser;
    private Long idProject;
    private LocalDateTime dateOfAddition;
}
