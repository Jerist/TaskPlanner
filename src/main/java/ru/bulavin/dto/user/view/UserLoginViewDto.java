package ru.bulavin.dto.user.view;

import lombok.Builder;

@Builder
public record UserLoginViewDto(String phone, String password) {
}
