package ru.bulavin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Project {
    private Long idProject;
    private String name;
    private String description;
    private Long idUser;
}
