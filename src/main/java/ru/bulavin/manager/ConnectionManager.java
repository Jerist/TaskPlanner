package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.processing.connection.ConcreteConnectionGetter;

@UtilityClass
public class ConnectionManager {
    @Getter
    private static final ConcreteConnectionGetter connectionGetter;

    static {
        connectionGetter = new ConcreteConnectionGetter();
    }
}
