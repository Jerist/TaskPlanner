package ru.bulavin.servicelayer.validator;

import org.junit.jupiter.api.Test;
import ru.bulavin.dto.user.controller.UserRegistrationControllerDto;
import ru.bulavin.processing.check.EmailCheck;
import ru.bulavin.processing.check.NameCheck;
import ru.bulavin.processing.check.PasswordCheck;
import ru.bulavin.processing.check.PhoneCheck;
import ru.bulavin.processing.validator.RegistrationUserValidator;
import ru.bulavin.processing.validator.load.LoadError;
import ru.bulavin.processing.validator.load.LoadValidationResult;
import ru.bulavin.processing.validator.load.TypeLoadError;
import ru.bulavin.repository.UserExistsDao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserValidatorTest {
    private final UserExistsDao userExistsDao = mock(UserExistsDao.class);
    private final RegistrationUserValidator userValidator;
    {
        EmailCheck emailCheck = new EmailCheck();
        NameCheck nameCheck = new NameCheck();
        PasswordCheck passwordCheck = new PasswordCheck();
        PhoneCheck phoneCheck = new PhoneCheck();
        userValidator = new RegistrationUserValidator(userExistsDao, nameCheck, phoneCheck, emailCheck, passwordCheck);
    }

    @Test
    void emptyNameTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("")
                .email("correct@email.com")
                .phone("+71234567890")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.EMPTY);
    }

    @Test
    void emptyEmailTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name")
                .email("")
                .phone("+71234567890")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.EMPTY);
    }

    @Test
    void emptyPhoneTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name")
                .email("correct@email.com")
                .phone("")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.EMPTY);
    }

    @Test
    void emptyPasswordTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name")
                .email("correct@email.com")
                .phone("+71234567890")
                .password("")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.EMPTY);
    }
    @Test
    void incorrectNameTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name!+23cwec")
                .email("correct@email.com")
                .phone("+71234567890")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.INCORRECT);
    }
    @Test
    void incorrectEmailTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name")
                .email("abra cadabra")
                .phone("+71234567890")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.INCORRECT);
    }
    @Test
    void incorrectPhoneTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name")
                .email("correct@email.com")
                .phone("666")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.INCORRECT);
    }
    @Test
    void existsPhoneTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name")
                .email("correct@email.com")
                .phone("+71234567890")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(true);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(false);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.NON_UNIQUE);
    }
    @Test
    void existsEmailTest() {
        UserRegistrationControllerDto user = UserRegistrationControllerDto.builder()
                .name("name")
                .email("correct@email.com")
                .phone("+71234567890")
                .password("123")
                .build();
        when(userExistsDao.existsByPhone(user.phone())).thenReturn(false);
        when(userExistsDao.existsByEmail(user.email())).thenReturn(true);
        LoadValidationResult loadValidationResult = userValidator.isValid(user);
        assertThat(loadValidationResult.getLoadErrors()).hasSize(1);
        LoadError error = loadValidationResult.getLoadErrors().getFirst();
        assertEquals(error.getType(), TypeLoadError.NON_UNIQUE);
    }
}