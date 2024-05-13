package ru.bulavin.dto.user.view;

import lombok.Builder;

@Builder
public record UserUpdatePasswordViewDto(String phone, String oldPassword, String newPassword) {
}
