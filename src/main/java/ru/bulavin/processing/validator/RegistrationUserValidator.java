package ru.bulavin.processing.validator;

import ru.bulavin.dto.user.controller.UserRegistrationControllerDto;
import ru.bulavin.dto.user.view.UserRegistrationViewDto;
import ru.bulavin.processing.check.EmailCheck;
import ru.bulavin.processing.check.NameCheck;
import ru.bulavin.processing.check.PhoneCheck;
import ru.bulavin.processing.validator.load.LoadError;
import ru.bulavin.processing.validator.load.LoadValidationResult;
import ru.bulavin.processing.validator.load.TypeLoadError;
import ru.bulavin.repository.UserExistsDao;
import ru.bulavin.processing.check.PasswordCheck;

public class RegistrationUserValidator {
    private final UserExistsDao userExistsDao;
    private final EmailCheck emailCheck;
    private final NameCheck nameCheck;
    private final PasswordCheck passwordCheck;
    private final PhoneCheck phoneCheck;

    public RegistrationUserValidator(UserExistsDao userExistsDao, NameCheck nameCheck, PhoneCheck phoneCheck,
                                     EmailCheck emailCheck, PasswordCheck passwordCheck) {
        this.userExistsDao = userExistsDao;
        this.emailCheck = emailCheck;
        this.nameCheck = nameCheck;
        this.passwordCheck = passwordCheck;
        this.phoneCheck = phoneCheck;
    }

    public LoadValidationResult isValid(UserRegistrationControllerDto user) {
        LoadValidationResult result = new LoadValidationResult();
        checkName(user, result);
        checkPhone(user, result);
        checkEmail(user, result);
        checkPassword(user, result);
        return result;
    }

    private static boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void checkName(UserRegistrationControllerDto user, LoadValidationResult loadValidationResult) {
        String name = user.name();
        if(isEmptyString(name)) {
            loadValidationResult.add(LoadError.of("name", TypeLoadError.EMPTY));
        } else if(!nameCheck.isCorrect(name)) {
            loadValidationResult.add(LoadError.of("name", TypeLoadError.INCORRECT));
        }
    }

    private void checkEmail(UserRegistrationControllerDto user, LoadValidationResult loadValidationResult) {
        String email = user.email();
        if(isEmptyString(email)) {
            loadValidationResult.add(LoadError.of("email", TypeLoadError.EMPTY));
        } else if(!emailCheck.isCorrect(email)) {
            loadValidationResult.add(LoadError.of("email", TypeLoadError.INCORRECT));
        } else if(userExistsDao.existsByEmail(email)) {
            loadValidationResult.add(LoadError.of("email", TypeLoadError.NON_UNIQUE));
        }
    }

    private void checkPhone(UserRegistrationControllerDto user, LoadValidationResult loadValidationResult) {
        String phone = user.phone();
        if(isEmptyString(phone)) {
            loadValidationResult.add(LoadError.of("phone", TypeLoadError.EMPTY));
        } else if(!phoneCheck.isCorrect(phone)) {
            loadValidationResult.add(LoadError.of("phone", TypeLoadError.INCORRECT));
        } else if(userExistsDao.existsByPhone(phone)) {
            loadValidationResult.add(LoadError.of("phone", TypeLoadError.NON_UNIQUE));
        }
    }

    private void checkPassword(UserRegistrationControllerDto user, LoadValidationResult loadValidationResult) {
        String password = user.password();
        if(isEmptyString(password)) {
            loadValidationResult.add(LoadError.of("password", TypeLoadError.EMPTY));
        } else if(!passwordCheck.isCorrect(password)) {
            loadValidationResult.add(LoadError.of("password", TypeLoadError.INCORRECT));
        }
    }

}
