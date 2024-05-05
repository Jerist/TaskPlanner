package ru.bulavin.service.entity.user;

import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.processing.validator.login.LoginError;

import java.util.Optional;

public record ResultCheckLogin(LoginError loginError, Optional<UserControllerDto> userDto) {
}
