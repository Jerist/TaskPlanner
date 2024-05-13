package ru.bulavin.repository;

import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;
import ru.bulavin.entity.Task;
import ru.bulavin.entity.TaskInProject;
import ru.bulavin.exception.DaoException;
import ru.bulavin.processing.ConnectionGetter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskInProjectDao implements BaseDao<TaskInProject> {

    private final ConnectionGetter connectionGetter;

    public TaskInProjectDao(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }
    //language=PostgreSQL
    private static final String INSERT_TASK_IN_PROJECT = "INSERT INTO taskInProject (date_addition, id_task, id_project) " +
            "VALUES (NOW(), ?, ?);";

    //language=PostgreSQL
    private static final String DELETE_TASK_IN_PROJECT = "DELETE FROM taskinproject WHERE id_task_in_project = ?;;";

    //language=PostgreSQL
    private static final String SELECT_ALL_TASKS_BY_PROJECT_ID = "SELECT * FROM taskinproject " +
            "JOIN task ON taskinproject.id_task = task.id_task " +
            "WHERE taskinproject.id_project = ?;";

    //language=PostgreSQL
    private static final String SELECT_ALL_TASKS_IN_PROJECT = "SELECT * FROM taskinproject " +
            "JOIN task ON taskinproject.id_task = task.id_task;";

    //language=PostgreSQL
    private static final String SELECT_TASK_BY_ID = "SELECT * FROM taskinproject " +
            "JOIN task ON taskinproject.id_task = task.id_task " +
            "WHERE taskinproject.id_task_in_project = ?;";

    //language=PostgreSQL
    private static final String UPDATE_TASK_IN_PROJECT = "UPDATE taskinproject " +
            "SET id_project = ?, date_addition = NOW() WHERE id_task_in_project = ?;";

    //language=PostgreSQL
    private static final String SELECT_BY_TASK_AND_PROJECT = "SELECT * FROM taskinproject " +
            "JOIN task ON taskinproject.id_task = task.id_task " +
            "WHERE taskinproject.id_task = ? AND taskinproject.id_project = ?;";


    public void insert(Long idProject, Long idTask) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK_IN_PROJECT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, idProject);
            preparedStatement.setLong(2, idTask);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void insert(TaskInProject taskInProject) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK_IN_PROJECT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, taskInProject.getIdTask());
            preparedStatement.setLong(2, taskInProject.getIdProject());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                taskInProject.setIdTask(generatedKeys.getLong("id_task_in_project"));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<TaskInProject> selectAll() {
        List<TaskInProject> tasks = new ArrayList<>();
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASKS_IN_PROJECT)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(extractTaskFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return tasks;
    }

    public List<TaskInProject> selectByProjectId(Long id) {
        List<TaskInProject> tasks = new ArrayList<>();
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASKS_BY_PROJECT_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(extractTaskFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return tasks;
    }

    @Override
    public TaskInProject selectById(Long id) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TASK_BY_ID);) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractTaskFromResultSet(resultSet);
            }
            return null;
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    public TaskInProject selectByTaskAndProject(Long idTask, Long idProject) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_TASK_AND_PROJECT);) {
            preparedStatement.setLong(1, idTask);
            preparedStatement.setLong(2, idProject);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractTaskFromResultSet(resultSet);
            }
            return null;
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int delete(Long id) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK_IN_PROJECT)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int update(TaskInProject taskInProject) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK_IN_PROJECT)) {

            preparedStatement.setLong(1, taskInProject.getIdProject());
            preparedStatement.setLong(2, taskInProject.getIdTaskInProject());

            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    private TaskInProject extractTaskFromResultSet(ResultSet resultSet) throws SQLException {
        Long idTaskInProject = resultSet.getLong("id_task_in_project");
        LocalDateTime dateAddition = resultSet.getTimestamp("date_addition").toLocalDateTime();
        Long idProject = resultSet.getLong("id_project");
        Long idTask = resultSet.getLong("id_task");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
        Timestamp deadlineDate = resultSet.getTimestamp("deadline");
        LocalDateTime deadline = deadlineDate==null?null:deadlineDate.toLocalDateTime();
        Status status = Status.valueOf(resultSet.getString("status"));
        Priority priority = Priority.valueOf(resultSet.getString("priority"));
        Long idUser = resultSet.getLong("id_user");
        return TaskInProject.builder()
                .idTaskInProject(idTaskInProject)
                .dateOfAddition(dateAddition)
                .idProject(idProject)
                .idTask(idTask)
                .name(name)
                .description(description)
                .dateStart(startDate)
                .deadline(deadline)
                .status(status)
                .priority(priority)
                .idUser(idUser)
                .build();
    }

}
