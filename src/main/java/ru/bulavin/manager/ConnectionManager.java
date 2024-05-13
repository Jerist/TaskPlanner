package ru.bulavin.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.bulavin.processing.ConnectionGetter;

@UtilityClass
public class ConnectionManager {
    @Getter
    private static final ConnectionGetter connectionGetter;

    static {
        connectionGetter = new ConnectionGetter();
    }
}
