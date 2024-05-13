package ru.bulavin.dto.user.view;

import lombok.Builder;

@Builder
public record UserRegistrationViewDto(String name, String phone, String email, String password) {
}
