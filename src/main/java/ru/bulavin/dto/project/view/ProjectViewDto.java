package ru.bulavin.dto.project.view;

import lombok.Builder;

@Builder
public record ProjectViewDto(String idProject, String name, String idUser) {
}
