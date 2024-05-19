package ru.bulavin.repository;

import ru.bulavin.exception.DaoException;
import ru.bulavin.processing.connection.ConnectionGetter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserExistsDao {
    private final ConnectionGetter connectionGetter;

    public UserExistsDao(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }

    //language=PostgreSQL
    private static final String SELECT_1_BY_EMAIL = "SELECT 1 FROM \"user\" WHERE email = ?;";

    //language=PostgreSQL
    private static final String SELECT_1_BY_PHONE = "SELECT 1 FROM \"user\" WHERE phone = ?;";

    public boolean existsByEmail(String email) {
        return executeQuery(email, SELECT_1_BY_EMAIL);
    }

    public boolean existsByPhone(String phone) {
        return executeQuery(phone, SELECT_1_BY_PHONE);
    }

    private boolean executeQuery(String param, String query) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, param);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }
}
