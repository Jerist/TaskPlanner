package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.processing.validator.RegistrationUserValidator;

@UtilityClass
public class ValidationManager {
    @Getter
    private static final RegistrationUserValidator registrationUserValidator;

    static {
        registrationUserValidator = new RegistrationUserValidator(DaoManager.getUserExistsDao(),
                CheckManager.getNameCheck(), CheckManager.getPhoneCheck(),
                CheckManager.getEmailCheck(), CheckManager.getPasswordCheck());
    }
}
