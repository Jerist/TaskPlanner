package ru.bulavin.repository;

import ru.bulavin.dto.user.controller.UserPasswordAndSaltControllerDto;
import ru.bulavin.entity.User;
import ru.bulavin.exception.DaoException;
import ru.bulavin.processing.connection.ConnectionGetter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements BaseDao<User>{
    private final ConnectionGetter connectionGetter;

    public UserDao(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }

    //language=PostgreSQL
    private final String SELECT_ALL_USERS = "SELECT * FROM \"user\";";

    //language=PostgreSQL
    private final String SELECT_USER_BY_ID = "SELECT * FROM \"user\" WHERE id_user = ?;";

    //language=PostgreSQL
    private final String SELECT_BY_LOGIN = "SELECT * FROM \"user\" WHERE phone = ?;";

    //language=PostgreSQL
    private final String SELECT_PASSWORD_SALT_USER = "SELECT password, salt FROM \"user\" WHERE phone = ?;";

    //language=PostgreSQL
    private final String INSERT_USER = "INSERT INTO \"user\"(name, phone, email, salt, password) VALUES (?, ?, ?, ?, ?);";

    //language=PostgreSQL
    private final String DELETE_USER = "DELETE FROM \"user\" WHERE id_user = ?;";

    //language=PostgreSQL
    private final String UPDATE_USER = "UPDATE \"user\" SET name = ?, phone = ?, email = ?, salt = ?, password = ? " +
            "WHERE id_user = ?;";

    @Override
    public boolean insert(User user) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(user, preparedStatement);

            boolean result = preparedStatement.executeUpdate() > 0;

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setIdUser(generatedKeys.getLong("id_user"));
            }
            return result;
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(extractUserFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    public User selectById(Long id) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return null;
    }


    @Override
    public int delete(Long id) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int update(User user) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {

            setStatement(user, preparedStatement);
            preparedStatement.setLong(6, user.getIdUser());

            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    public User selectByLogin(String phone) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_LOGIN)) {

            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return null;
    }

    public UserPasswordAndSaltControllerDto selectPasswordByLogin(String login) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PASSWORD_SALT_USER)) {
            String password = null;
            String salt = null;
            preparedStatement.setString(1, login);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    password = rs.getString("password");
                    salt = rs.getString("salt");
                }
            }
            return UserPasswordAndSaltControllerDto.builder()
                    .salt(Optional.ofNullable(salt))
                    .password(Optional.ofNullable(password))
                    .build();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    private void setStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPhone());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getSalt());
        preparedStatement.setString(5, user.getPassword());
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_user");
        String name = resultSet.getString("name");
        String phone = resultSet.getString("phone").trim();
        String email = resultSet.getString("email");
        String salt = resultSet.getString("salt");
        String password = resultSet.getString("password");
        return User.builder()
                .idUser(id)
                .name(name)
                .phone(phone)
                .email(email)
                .salt(salt)
                .password(password)
                .build();
    }
}
