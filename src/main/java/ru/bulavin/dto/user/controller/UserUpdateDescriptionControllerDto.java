package ru.bulavin.dto.user.controller;

import lombok.Builder;

@Builder
public record UserUpdateDescriptionControllerDto(String name, String phone, String email) {
}
