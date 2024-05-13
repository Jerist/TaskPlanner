package ru.bulavin.dto.user.controller;

import lombok.Builder;

import java.util.Optional;

@Builder
public record UserPasswordAndSaltControllerDto(Optional<String> salt, Optional<String> password) {
}
