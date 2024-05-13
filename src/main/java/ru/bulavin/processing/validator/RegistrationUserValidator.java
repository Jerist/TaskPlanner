package ru.bulavin.processing.validator;

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

    public LoadValidationResult isValid(UserRegistrationViewDto userRegistrationViewDto) {
        LoadValidationResult result = new LoadValidationResult();
        checkName(userRegistrationViewDto, result);
        checkPhone(userRegistrationViewDto, result);
        checkEmail(userRegistrationViewDto, result);
        checkPassword(userRegistrationViewDto, result);
        return result;
    }

    private static boolean isEmptyString(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void checkName(UserRegistrationViewDto userRegistrationViewDto, LoadValidationResult loadValidationResult) {
        String name = userRegistrationViewDto.name();
        if(isEmptyString(name)) {
            loadValidationResult.add(LoadError.of("name", TypeLoadError.EMPTY));
        } else if(!nameCheck.isCorrect(name)) {
            loadValidationResult.add(LoadError.of("name", TypeLoadError.INCORRECT));
        }
    }

    private void checkEmail(UserRegistrationViewDto userRegistrationViewDto, LoadValidationResult loadValidationResult) {
        String email = userRegistrationViewDto.email();
        if(isEmptyString(email)) {
            loadValidationResult.add(LoadError.of("email", TypeLoadError.EMPTY));
        } else if(!emailCheck.isCorrect(email)) {
            loadValidationResult.add(LoadError.of("email", TypeLoadError.INCORRECT));
        } else if(userExistsDao.existByEmail(email)) {
            loadValidationResult.add(LoadError.of("email", TypeLoadError.NON_UNIQUE));
        }
    }

    private void checkPhone(UserRegistrationViewDto userRegistrationViewDto, LoadValidationResult loadValidationResult) {
        String phone = userRegistrationViewDto.phone();
        if(isEmptyString(phone)) {
            loadValidationResult.add(LoadError.of("phone", TypeLoadError.EMPTY));
        } else if(!phoneCheck.isCorrect(phone)) {
            loadValidationResult.add(LoadError.of("phone", TypeLoadError.INCORRECT));
        } else if(userExistsDao.existByPhone(phone)) {
            loadValidationResult.add(LoadError.of("phone", TypeLoadError.NON_UNIQUE));
        }
    }

    private void checkPassword(UserRegistrationViewDto userRegistrationViewDto, LoadValidationResult loadValidationResult) {
        String password = userRegistrationViewDto.password();
        if(isEmptyString(password)) {
            loadValidationResult.add(LoadError.of("password", TypeLoadError.EMPTY));
        } else if(!passwordCheck.isCorrect(password)) {
            loadValidationResult.add(LoadError.of("password", TypeLoadError.INCORRECT));
        }
    }

}
