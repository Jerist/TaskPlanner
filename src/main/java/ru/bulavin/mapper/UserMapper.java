package ru.bulavin.mapper;

import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.dto.user.controller.UserLoginControllerDto;
import ru.bulavin.dto.user.controller.UserRegistrationControllerDto;
import ru.bulavin.dto.user.view.UserLoginViewDto;
import ru.bulavin.dto.user.view.UserRegistrationViewDto;
import ru.bulavin.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserMapper {
    public User map(UserRegistrationControllerDto obj, String salt, BiFunction<String, String, String> function) {
        return User.builder()
                .name(obj.name())
                .phone(obj.phone())
                .email(obj.email())
                .password(function.apply(obj.password(), salt))
                .salt(salt)
                .build();
    }
    public UserLoginControllerDto map(UserLoginViewDto obj) {
        return UserLoginControllerDto.builder()
                .phone(obj.phone())
                .password(obj.password())
                .build();
    }
    public UserControllerDto map(User obj) {
        return UserControllerDto.builder()
                .idUser(obj.getIdUser())
                .name(obj.getName())
                .phone(obj.getPhone())
                .email(obj.getEmail())
                .password(obj.getPassword())
                .build();
    }

    public UserRegistrationControllerDto map(UserRegistrationViewDto obj) {
        return UserRegistrationControllerDto.builder()
                .name(obj.name())
                .phone(obj.phone())
                .email(obj.email())
                .password(obj.password())
                .build();
    }
}
