package ru.bulavin.dto.user.view;

import lombok.Builder;

@Builder
public record UserViewDto(Long idUser,
                          String name,
                          String phone,
                          String email,
                          String password) {
}
