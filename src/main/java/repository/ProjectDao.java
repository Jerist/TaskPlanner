package repository;

import entity.Project;
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

public class ProjectDao implements BaseDao<Project> {
    private final ConnectionManager connectionManager;
    UserDao userDao;

    public ProjectDao(ConnectionManager connectionManager, UserDao userDao) {
        this.connectionManager = connectionManager;
        this.userDao = userDao;
    }

    //language=PostgreSQL
    private final String SELECT_ALL_PROJECT = "SELECT * FROM project;";

    //language=PostgreSQL
    private final String SELECT_PROJECT_BY_ID = "SELECT * FROM project WHERE id_project = ?;";

    //language=PostgreSQL
    private final String SELECT_PROJECTS_BY_USER_ID = "SELECT * FROM project WHERE id_user = ?;";

    //language=PostgreSQL
    private final String INSERT_PROJECT = "INSERT INTO project (name, id_user) VALUES (?, ?);";

    //language=PostgreSQL
    private final String UPDATE_PROJECT = "UPDATE project SET name = ?, id_user = ? WHERE id_project = ?;";

    //language=PostgreSQL
    private final String DELETE_PROJECT = "DELETE FROM project WHERE id_project = ?;";


    @Override
    public void insert(Project project) {
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(project, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                project.setIdProject(generatedKeys.getLong("id_project"));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Project> selectAll() {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PROJECT);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                projects.add(extractProjectFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return projects;
    }

    @Override
    public Project selectById(Long id) {
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECT_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractProjectFromResultSet(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return null;
    }

    @Override
    public int delete(Long id) {
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int update(Project project) {
        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT)) {

            setStatement(project, preparedStatement);
            preparedStatement.setLong(3, project.getIdProject());

            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    public List<Project> selectAllByUserId(Long id) {
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECTS_BY_USER_ID);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            preparedStatement.setLong(1, id);
            while (resultSet.next()) {
                projects.add(extractProjectFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return projects;
    }

    private void setStatement(Project project, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, project.getName());
        preparedStatement.setLong(2, project.getUser().getIdUser());
    }

    private Project extractProjectFromResultSet(ResultSet resultSet) throws SQLException {
        Long idProject = resultSet.getLong("id_project");
        String name = resultSet.getString("name");
        User user = userDao.selectById(resultSet.getLong("id_user"));
        return Project.builder()
                .idProject(idProject)
                .name(name)
                .user(user)
                .build();
    }
}
