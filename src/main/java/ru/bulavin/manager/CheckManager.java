package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.processing.check.EmailCheck;
import ru.bulavin.processing.check.NameCheck;
import ru.bulavin.processing.check.PasswordCheck;
import ru.bulavin.processing.check.PhoneCheck;

@UtilityClass
public class CheckManager {
    @Getter
    private static final EmailCheck emailCheck;

    @Getter
    private static final NameCheck nameCheck;

    @Getter
    private static final PasswordCheck passwordCheck;

    @Getter
    private static final PhoneCheck phoneCheck;

    static {
        emailCheck = new EmailCheck();
        nameCheck = new NameCheck();
        passwordCheck = new PasswordCheck();
        phoneCheck = new PhoneCheck();
    }
}
