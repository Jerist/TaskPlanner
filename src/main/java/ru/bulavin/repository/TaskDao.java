package ru.bulavin.repository;

import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;
import ru.bulavin.entity.Task;
import ru.bulavin.exception.DaoException;
import ru.bulavin.processing.ConnectionGetter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDao implements BaseDao<Task> {
    private final ConnectionGetter connectionGetter;

    public TaskDao(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }


    //language=PostgreSQL
    private static final String INSERT_TASK = "INSERT INTO task (name, description, start_date, deadline, status, " +
            "priority, id_user)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";

    //language=PostgreSQL
    private static final String SELECT_ALL_TASKS = "SELECT * FROM task;";

    //language=PostgreSQL
    private static final String SELECT_ACTIVE_TASKS_BY_USER_ID = "SELECT * FROM task WHERE status='InProcess' " +
            "AND start_date <= NOW() AND id_user = ?;";

    //language=PostgreSQL
    private static final String SELECT_TASK_BY_ID ="SELECT * FROM task WHERE id_task = ?;";

    //language=PostgreSQL
    private static final String SELECT_TASKS_BY_USER_ID = "SELECT * FROM task WHERE task.id_user = ?;";

    //language=PostgreSQL
    private static final String SELECT_TASKS_BY_PROJECT_ID = "SELECT * FROM task JOIN taskinproject ON " +
            "task.id_task = taskinproject.id_task WHERE taskinproject.id_project = ?;";

    //language=PostgreSQL
    private static final String DELETE_TASK = "DELETE FROM task WHERE id_task = ?;";

    //language=PostgreSQL
    private static final String UPDATE_TASK = "UPDATE task " +
            "SET name = ?, description = ?, start_date = ?, deadline = ?, status = ?, priority = ?, id_user = ? " +
            "WHERE id_task = ?;";


    @Override
    public void insert(Task task) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(task, preparedStatement);

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                task.setIdTask(generatedKeys.getLong("id_task"));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Task> selectAll() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASKS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                tasks.add(extractTaskFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return tasks;
    }

    @Override
    public Task selectById(Long id) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TASK_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return extractTaskFromResultSet(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return null;
    }

    @Override
    public int delete(Long id) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int update(Task task) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK)) {

            setStatement(task, preparedStatement);
            preparedStatement.setLong(8, task.getIdTask());

            return preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
    }

    public List<Task> selectActiveTasksByUserId(Long id) {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTIVE_TASKS_BY_USER_ID)) {

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

    public List<Task> selectByUserId(Long id) {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TASKS_BY_USER_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tasks.add(extractTaskFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return tasks;
    };

    public List<Task> selectByProjectId(Long id) {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = connectionGetter.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TASKS_BY_PROJECT_ID);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            preparedStatement.setLong(1, id);
            while (resultSet.next()) {
                tasks.add(extractTaskFromResultSet(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            throw new DaoException(e);
        }
        return tasks;
    };

    private void setStatement(Task task, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, task.getName());
        preparedStatement.setString(2, task.getDescription());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getDateStart()));
        preparedStatement.setTimestamp(4, task.getDeadline()==null?null:Timestamp.valueOf(task.getDeadline()));
        preparedStatement.setString(5, task.getStatus().toString());
        preparedStatement.setString(6, task.getPriority().toString());
        preparedStatement.setLong(7, task.getIdUser());
    }
    private Task extractTaskFromResultSet(ResultSet resultSet) throws SQLException {
        Long idTask = resultSet.getLong("id_task");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDateTime startDate = resultSet.getTimestamp("start_date").toLocalDateTime();
        Timestamp deadlineDate = resultSet.getTimestamp("deadline");
        LocalDateTime deadline = deadlineDate==null?null:deadlineDate.toLocalDateTime();
        Status status = Status.valueOf(resultSet.getString("status"));
        Priority priority = Priority.valueOf(resultSet.getString("priority"));
        Long idUser = resultSet.getLong("id_user");
        return Task.builder()
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
