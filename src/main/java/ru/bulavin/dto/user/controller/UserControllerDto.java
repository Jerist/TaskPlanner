package ru.bulavin.dto.user.controller;

import lombok.Builder;

@Builder
public record UserControllerDto(Long idUser,
                                String name,
                                String phone,
                                String email,
                                String password) {
}
