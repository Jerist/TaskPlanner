package ru.bulavin.dto.project.controller;

import lombok.Builder;

@Builder
public record ProjectControllerDto(Long idProject, String name, Long idUser) {
}
