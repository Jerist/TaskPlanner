package ru.bulavin.processing.check;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class NameCheck {
    private static final String NAME_PATTERN = "[a-zA-Z\\d-_]{1,20}";

    public boolean isCorrect(String name) {
        return Pattern.matches(NAME_PATTERN, name);
    }
}
