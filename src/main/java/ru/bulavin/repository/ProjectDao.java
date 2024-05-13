package ru.bulavin.repository;

import ru.bulavin.entity.Project;
import ru.bulavin.entity.User;
import ru.bulavin.exception.DaoException;
import ru.bulavin.processing.ConnectionGetter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao implements BaseDao<Project> {
    private final ConnectionGetter connectionGetter;
    UserDao userDao;

    public ProjectDao(ConnectionGetter connectionGetter, UserDao userDao) {
        this.connectionGetter = connectionGetter;
        this.userDao = userDao;
    }

    //language=PostgreSQL
    private final String SELECT_ALL_PROJECT = "SELECT * FROM project;";

    //language=PostgreSQL
    private final String SELECT_PROJECT_BY_ID = "SELECT * FROM project WHERE id_project = ?;";

    //language=PostgreSQL
    private final String SELECT_PROJECTS_BY_USER_ID = "SELECT * FROM project WHERE id_user = ?;";

    //language=PostgreSQL
    private final String INSERT_PROJECT = "INSERT INTO project (name, description, id_user) VALUES (?, ?, ?);";

    //language=PostgreSQL
    private final String UPDATE_PROJECT = "UPDATE project SET name = ?, description = ?," +
            " id_user = ? WHERE id_project = ?;";

    //language=PostgreSQL
    private final String DELETE_PROJECT = "DELETE FROM project WHERE id_project = ?;";


    @Override
    public void insert(Project project) {
        try (Connection connection = connectionGetter.get();
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
        try (Connection connection = connectionGetter.get();
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
        try (Connection connection = connectionGetter.get();
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
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int update(Project project) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT)) {

            setStatement(project, preparedStatement);
            preparedStatement.setLong(4, project.getIdProject());

            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    public List<Project> selectAllByUserId(Long id) {
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECTS_BY_USER_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        preparedStatement.setString(2, project.getDescription());
        preparedStatement.setLong(3, project.getIdUser());
    }

    private Project extractProjectFromResultSet(ResultSet resultSet) throws SQLException {
        Long idProject = resultSet.getLong("id_project");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        Long idUser = resultSet.getLong("id_user");
        return Project.builder()
                .idProject(idProject)
                .name(name)
                .description(description)
                .idUser(idUser)
                .build();
    }
}
