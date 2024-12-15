package ru.bulavin.servicelayer.service;

import org.junit.jupiter.api.Test;
import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.dto.user.controller.UserLoginControllerDto;
import ru.bulavin.dto.user.controller.UserPasswordAndSaltControllerDto;
import ru.bulavin.entity.User;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.mapper.UserMapper;
import ru.bulavin.processing.PasswordHashed;
import ru.bulavin.processing.validator.login.LoginError;
import ru.bulavin.repository.UserDao;
import ru.bulavin.service.UserService;
import ru.bulavin.service.entity.user.ResultCheckLogin;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private final PasswordHashed passwordHashed = new PasswordHashed();
    private final UserMapper userMapper =  MapperManager.getUserMapper();
    private final UserDao userDao;
    {
        userDao = mock(UserDao.class);
    }
    private final UserService userService = new UserService(MapperManager.getUserMapper(), passwordHashed, userDao);

    @Test
    void notExistsUser() {
        UserPasswordAndSaltControllerDto userPasswordAndSaltControllerDto = UserPasswordAndSaltControllerDto.builder()
                .password(Optional.empty())
                .salt(Optional.empty())
                .build();

        UserLoginControllerDto user = UserLoginControllerDto.builder()
                .phone("+71234567890")
                .build() ;

        when(userDao.selectPasswordByLogin(user.phone())).thenReturn(userPasswordAndSaltControllerDto);

        ResultCheckLogin result = userService.checkLogin(user);
        assertEquals(result.userDto(), Optional.empty());
        assertEquals(result.loginError(), LoginError.USER_NOT_FOUND);
    }

    @Test
    void incorrectPasswordUser() {
        UserPasswordAndSaltControllerDto userPasswordAndSaltControllerDto = UserPasswordAndSaltControllerDto.builder()
                .password(Optional.of("321"))
                .salt(Optional.of("salt"))
                .build();

        UserLoginControllerDto user = UserLoginControllerDto.builder()
                .phone("+71234567890")
                .password("123")
                .build() ;

        when(userDao.selectPasswordByLogin(user.phone())).thenReturn(userPasswordAndSaltControllerDto);

        ResultCheckLogin result = userService.checkLogin(user);
        assertEquals(result.userDto(), Optional.empty());
        assertEquals(result.loginError(), LoginError.INCORRECT_PASSWORD);
    }
    @Test
    void correctUser() {
        User returnUser = User.builder()
                .name("user")
                .phone("+71234567890")
                .email("email@email.com")
                .password("123")
                .salt("user")
                .build();

        UserPasswordAndSaltControllerDto userPasswordAndSaltControllerDto = UserPasswordAndSaltControllerDto.builder()
                .password(Optional.of(passwordHashed.hashPassword(returnUser.getPassword(), "salt")))
                .salt(Optional.of("salt"))
                .build();

        UserLoginControllerDto user = UserLoginControllerDto.builder()
                .phone("+71234567890")
                .password("123")
                .build() ;

        when(userDao.selectPasswordByLogin(user.phone())).thenReturn(userPasswordAndSaltControllerDto);
        when(userDao.selectByLogin(user.phone())).thenReturn(returnUser);

        ResultCheckLogin result = userService.checkLogin(user);
        assertTrue(result.userDto().isPresent());
        UserControllerDto userControllerDto = userMapper.map(returnUser);
        assertEquals(result.userDto().get(), userControllerDto);
        assertNull(result.loginError());
    }
}
