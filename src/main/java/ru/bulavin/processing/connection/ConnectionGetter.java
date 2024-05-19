package ru.bulavin.processing.connection;

import java.sql.Connection;

public interface ConnectionGetter {
    public Connection get() throws InterruptedException;
}
