package repository;

import entity.User;
import exception.DaoException;
import singleton.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements BaseDao<User>{
    private final ConnectionManager connectionManager;

    public UserDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    //language=PostgreSQL
    private final String SELECT_ALL_USERS = "SELECT * FROM \"user\";";

    //language=PostgreSQL
    private final String SELECT_USER_BY_ID = "SELECT * FROM \"user\" WHERE id_user = ?;";

    //language=PostgreSQL
    private final String INSERT_USER = "INSERT INTO \"user\" VALUES (?, ?, ?, ?);";

    //language=PostgreSQL
    private final String DELETE_USER = "DELETE FROM \"user\" WHERE id_user = ?;";

    //language=PostgreSQL
    private final String UPDATE_USER = "UPDATE \"user\" SET name = ?, phone = ?, email = ?, password = ? " +
            "WHERE id_user = ?;";

    @Override
    public void insert(User user) {
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(user, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setIdUser(generatedKeys.getLong("id_task"));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionManager.get();
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
        try (Connection connection = connectionManager.get();
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
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int update(User user) {
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {

            setStatement(user, preparedStatement);
            preparedStatement.setLong(5, user.getIdUser());

            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    private void setStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPhone());
        preparedStatement.setString(3, user.getEmail());
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        String phone = resultSet.getString("phone");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        return User.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .password(password)
                .build();
    }
}
