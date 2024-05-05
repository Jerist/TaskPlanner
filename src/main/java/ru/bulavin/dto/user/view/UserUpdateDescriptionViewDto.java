package ru.bulavin.dto.user.view;

import lombok.Builder;

@Builder
public record UserUpdateDescriptionViewDto(String name, String phone, String email) {
}
