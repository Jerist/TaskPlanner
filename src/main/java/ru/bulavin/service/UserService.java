package ru.bulavin.service;

import ru.bulavin.dto.user.controller.UserControllerDto;
import ru.bulavin.dto.user.controller.UserLoginControllerDto;
import ru.bulavin.dto.user.controller.UserPasswordAndSaltControllerDto;
import ru.bulavin.dto.user.controller.UserRegistrationControllerDto;
import ru.bulavin.entity.User;
import ru.bulavin.mapper.UserMapper;
import ru.bulavin.processing.PasswordHashed;
import ru.bulavin.processing.validator.login.LoginError;
import ru.bulavin.repository.UserDao;
import ru.bulavin.service.entity.user.ResultCheckLogin;

import java.util.Optional;

public class UserService {
    private final UserMapper userMapper;
    private final PasswordHashed passwordHashed;
    private final UserDao userDao;
    //private final UpdateUserValidator updateUserValidator;
    public UserService(UserMapper userMapper, PasswordHashed passwordHashed, UserDao userDao/*,
                       UpdateUserValidator updateUserValidator*/) {
        this.userMapper = userMapper;
        this.passwordHashed = passwordHashed;
        this.userDao = userDao;
        //this.updateUserValidator = updateUserValidator;
    }
    public void insertUser(UserRegistrationControllerDto userRegistrationControllerDto) {
        String salt = userRegistrationControllerDto.name();
        User user = userMapper.map(userRegistrationControllerDto, salt, passwordHashed::hashPassword);
        userDao.insert(user);
    }

    public ResultCheckLogin checkLogin(UserLoginControllerDto userLoginControllerDto) {
        UserPasswordAndSaltControllerDto userPasswordAndSaltControllerDto =
                userDao.selectPasswordByLogin(userLoginControllerDto.phone());
        if (userPasswordAndSaltControllerDto.password().isPresent() && userPasswordAndSaltControllerDto.salt().isPresent()) {
            if (userPasswordAndSaltControllerDto.password().get()
                    .equals(passwordHashed.hashPassword(userLoginControllerDto.password(),
                            userPasswordAndSaltControllerDto.salt().get()))) {
                User user = userDao.selectByLogin(userLoginControllerDto.phone());
                UserControllerDto userControllerDto = userMapper.map(user);
                return new ResultCheckLogin(null, Optional.of(userControllerDto));
            } else {
                return new ResultCheckLogin(LoginError.INCORRECT_PASSWORD, Optional.empty());
            }
        } else {
            return new ResultCheckLogin(LoginError.USER_NOT_FOUND, Optional.empty());
        }
    }


}
