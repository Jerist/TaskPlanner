package ru.bulavin.processing.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PasswordCheck {
    private static final String PASSWORD_PATTEN = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S[a-zA-Z\\d]{8,20}";

    public boolean isCorrect(String string) {
        return Pattern.matches(PASSWORD_PATTEN, string);
    }
}
