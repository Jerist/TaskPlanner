package ru.bulavin.processing.validator.load;

import lombok.Value;

@Value(staticConstructor = "of")
public class LoadError {
    String field;
    TypeLoadError type;
}
