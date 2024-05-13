package ru.bulavin.processing.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PhoneCheck {
    private static final String PHONE_PATTERN = "(\\+?)\\d{11}";

    public boolean isCorrect(String phone) {
        return Pattern.matches(PHONE_PATTERN, phone);
    }
}
