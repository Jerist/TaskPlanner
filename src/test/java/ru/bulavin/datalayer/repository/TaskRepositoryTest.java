package ru.bulavin.datalayer.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.bulavin.datalayer.TestConnectionGetter;
import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Status;
import ru.bulavin.entity.Task;
import ru.bulavin.entity.User;
import ru.bulavin.exception.DaoException;
import ru.bulavin.repository.TaskDao;
import ru.bulavin.repository.UserDao;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class TaskRepositoryTest {
    private final TestConnectionGetter connectionGetter = TestConnectionGetter.getInstance();
    private final TaskDao taskRepository = new TaskDao(connectionGetter);
    private final UserDao userDao = new UserDao(connectionGetter);

    private static User generateDefaultUser() {
        return User.builder()
                .name("user1")
                .email("user1@email.com")
                .phone("+71234567890")
                .password("123")
                .salt("salt1")
                .build();
    }

    private static Stream<Task> generateInvalidTasks() {
        Task task1 = Task.builder()
                .name(null)
                .description("Description 1")
                .dateStart(LocalDateTime.now())
                .deadline(LocalDateTime.now().plusDays(1))
                .status(Status.InProcess)
                .priority(Priority.Medium)
                .idUser(1L)
                .build();

        Task task2 = Task.builder()
                .name("Task 2")
                .description(null)
                .dateStart(LocalDateTime.now())
                .deadline(LocalDateTime.now().plusDays(1))
                .status(Status.InProcess)
                .priority(Priority.Medium)
                .idUser(1L)
                .build();

        Task task3 = Task.builder()
                .name("Task 3")
                .description("Description 3")
                .dateStart(null)
                .deadline(LocalDateTime.now().plusDays(1))
                .status(Status.InProcess)
                .priority(Priority.Medium)
                .idUser(1L)
                .build();

        Task task4 = Task.builder()
                .name("Task 4")
                .description("Description 4")
                .dateStart(LocalDateTime.now())
                .deadline(LocalDateTime.now().plusDays(1))
                .status(Status.InProcess)
                .priority(null)
                .idUser(1L)
                .build();

        Task task5 = Task.builder()
                .name("Task 5")
                .description("Description 5")
                .dateStart(LocalDateTime.now())
                .deadline(LocalDateTime.now().plusDays(1))
                .status(null)
                .priority(Priority.Medium)
                .idUser(1L)
                .build();
        return Stream.of(task1, task2, task3, task4, task5);
    }

    private static Stream<Task> generateValidTasks() {
        return generateValidTasksWithId(1L);
    }
    private static Stream<Task> generateValidTasksWithId(Long idUser) {
        Task task1 = Task.builder()
                .name("Task 1")
                .description("Description 1")
                .dateStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .deadline(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .status(Status.InProcess)
                .priority(Priority.Medium)
                .idUser(idUser)
                .build();

        Task task2 = Task.builder()
                .name("Task 2")
                .description("Description 2")
                .dateStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .deadline(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .status(Status.Completed)
                .priority(Priority.High)
                .idUser(idUser)
                .build();

        Task task3 = Task.builder()
                .name("Task 3")
                .description("Description 3")
                .dateStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .deadline(LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.SECONDS))
                .status(Status.InProcess)
                .priority(Priority.Low)
                .idUser(idUser)
                .build();

        return Stream.of(task1, task2, task3);
    }

    @SneakyThrows
    @BeforeEach
    public void clear() {
        try (Connection connection = connectionGetter.get()) {
            //language=PostgreSQL
            String CLEAR_TABLE = "TRUNCATE taskplanner_test.public.task RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE).executeUpdate();
            //language=PostgreSQL
            String CLEAR_TABLE_USERS = "TRUNCATE taskplanner_test.public.user RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE_USERS).executeUpdate();
        }
    }

    @ParameterizedTest
    @MethodSource("generateInvalidTasks")
    void insertInvalidTaskTest(Task task) {
        assertThatThrownBy(() -> taskRepository.insert(task)).isInstanceOf(DaoException.class);
    }

    @ParameterizedTest
    @MethodSource("generateValidTasks")
    void insertValidTaskTest(Task task) {
        User user = generateDefaultUser();
        userDao.insert(user);
        task.setIdUser(user.getIdUser());
        assertThat(taskRepository.insert(task)).isTrue();

        Task insertedTask = taskRepository.selectById(task.getIdTask());
        assertEquals(task, insertedTask);
    }

    @Test
    void selectAllTasksTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        List<Task> tasks = generateValidTasksWithId(user.getIdUser()).toList();
        for(Task task : tasks) {
            taskRepository.insert(task);
        }

        List<Task> insertedTasks = taskRepository.selectAll();

        assertThat(tasks).hasSize(3);
        for(Task task : tasks) {
            assertTrue(insertedTasks.contains(task));
        }
    }

    @Test
    void deleteTaskTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        List<Task> tasks = new ArrayList<>(generateValidTasksWithId(user.getIdUser()).toList());
        for(Task task : tasks) {
            taskRepository.insert(task);
        }
        Task deletedTask = tasks.getFirst();
        assertEquals(1, taskRepository.delete(deletedTask.getIdTask()));
        List<Task> insertedTasks = taskRepository.selectAll();
        assertThat(insertedTasks).hasSize(tasks.size()-1);

        tasks.remove(deletedTask);
        for(Task task : tasks) {
            assertTrue(insertedTasks.contains(task));
        }
    }

    @Test
    void updateTaskTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        List<Task> tasks = new ArrayList<>(generateValidTasksWithId(user.getIdUser()).toList());
        for(Task task : tasks) {
            taskRepository.insert(task);
        }
        Task oldTask = tasks.getFirst();
        Task newTask = Task.builder()
                .idTask(oldTask.getIdTask())
                .name("new task")
                .description("new description")
                .dateStart(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .deadline(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .status(Status.Delayed)
                .priority(Priority.Low)
                .idUser(user.getIdUser())
                .build();
        assertEquals(taskRepository.update(newTask), 1);
        Task updatedTask = taskRepository.selectById(oldTask.getIdTask());
        assertEquals(updatedTask, newTask);
    }

    @Test
    void selectActiveTasksByUserIdTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        List<Task> tasks = new ArrayList<>(generateValidTasksWithId(user.getIdUser()).toList());
        for(Task task : tasks) {
            taskRepository.insert(task);
        }
        List<Task> activeTasks = tasks.stream().filter(t->t.getStatus().equals(Status.InProcess)).toList();
        List<Task> insertedActiveTasks = taskRepository.selectActiveTasksByUserId(user.getIdUser());

        assertThat(insertedActiveTasks).hasSize(activeTasks.size());
        for(Task task : activeTasks) {
            assertTrue(insertedActiveTasks.contains(task));
        }

    }

    @Test
    void selectTasksByUserIdTest() {
        User user = generateDefaultUser();
        User user2 = User.builder()
                .name("user2")
                .email("user2@email.com")
                .phone("+70987654321")
                .password("qwerty")
                .salt("salt2")
                .build();

        userDao.insert(user);
        List<Task> tasks = new ArrayList<>(generateValidTasksWithId(user.getIdUser()).toList());
        for(Task task : tasks) {
            taskRepository.insert(task);
        }

        userDao.insert(user2);
        List<Task> tasks2 = new ArrayList<>(generateValidTasksWithId(user2.getIdUser()).toList());
        for(Task task : tasks2) {
            taskRepository.insert(task);
        }

        List<Task> tasksByUserId = tasks.stream().filter(t-> Objects.equals(t.getIdUser(), user.getIdUser())).toList();
        List<Task> insertedTasks = taskRepository.selectByUserId(user.getIdUser());

        assertThat(insertedTasks).hasSize(tasksByUserId.size());
        for(Task task : tasksByUserId) {
            assertTrue(insertedTasks.contains(task));
        }

    }


}
