package ru.bulavin.dto.user.controller;

import lombok.Builder;

@Builder
public record UserUpdatePasswordControllerDto(String phone, String oldPassword, String newPassword) {
}
