package ru.bulavin.dto.user.controller;

import lombok.Builder;

@Builder
public record UserRegistrationControllerDto(String name, String phone, String email, String password) {
}
