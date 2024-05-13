package ru.bulavin.dto.user.controller;

import lombok.Builder;

@Builder
public record UserLoginControllerDto(String phone, String password) {
}
