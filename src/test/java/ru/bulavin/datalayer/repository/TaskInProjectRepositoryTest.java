package ru.bulavin.datalayer.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bulavin.datalayer.TestConnectionGetter;
import ru.bulavin.entity.Priority;
import ru.bulavin.entity.Project;
import ru.bulavin.entity.Status;
import ru.bulavin.entity.Task;
import ru.bulavin.entity.TaskInProject;
import ru.bulavin.entity.User;
import ru.bulavin.repository.ProjectDao;
import ru.bulavin.repository.TaskInProjectDao;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskInProjectRepositoryTest {
    private static final TestConnectionGetter connectionGetter = TestConnectionGetter.getInstance();
    private static final UserDao userDao = new UserDao(connectionGetter);
    private static final TaskDao taskDao = new TaskDao(connectionGetter);
    private static final ProjectDao projectDao = new ProjectDao(connectionGetter, userDao);
    private static final TaskInProjectDao taskInProjectRepository = new TaskInProjectDao(connectionGetter);

    private static User generateDefaultUser() {
        return User.builder()
                .name("user1")
                .email("user1@email.com")
                .phone("+71234567890")
                .password("123")
                .salt("salt1")
                .build();
    }

    private static Project generateDefaultProject(User user) {
        return Project.builder()
                .name("Project 1")
                .description("Description 1")
                .idUser(user.getIdUser())
                .build();
    }

    private static Stream<Task> generateTasks(Long idUser) {
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
            String CLEAR_TABLE = "TRUNCATE taskplanner_test.public.taskinproject RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE).executeUpdate();
            //language=PostgreSQL
            String CLEAR_TABLE_TASKS = "TRUNCATE taskplanner_test.public.task RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE_TASKS).executeUpdate();
            //language=PostgreSQL
            String CLEAR_TABLE_USERS = "TRUNCATE taskplanner_test.public.user RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE_USERS).executeUpdate();
            //language=PostgreSQL
            String CLEAR_TABLE_PROJECTS = "TRUNCATE taskplanner_test.public.project RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE_PROJECTS).executeUpdate();
        }
    }

    @Test
    void insertValidTaskInProjectTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        Project project = generateDefaultProject(user);
        projectDao.insert(project);

        Task task = generateTasks(user.getIdUser()).toList().getFirst();

        taskDao.insert(task);

        assertTrue(taskInProjectRepository.insert(project.getIdProject(), task.getIdTask()));
        List<TaskInProject> tasksInProject = taskInProjectRepository.selectByProjectId(project.getIdProject());
        assertThat(tasksInProject).hasSize(1);
        TaskInProject insertedTaskInProject = tasksInProject.getFirst();
        assertEquals(insertedTaskInProject.getIdProject(), project.getIdProject());
        assertEquals(insertedTaskInProject.getIdTask(), task.getIdTask());
    }
    @Test
    void selectAllTasksInProjectTest() {
        User user = generateDefaultUser();
        userDao.insert(user);

        Project project = generateDefaultProject(user);
        projectDao.insert(project);

        List<Task> tasks = generateTasks(user.getIdUser()).toList();
        for (Task task : tasks) {
            taskDao.insert(task);
        }
        for(Task task : tasks) {
            taskInProjectRepository.insert(project.getIdProject(), task.getIdTask());
        }
        List<TaskInProject> insertedTasksInProject = taskInProjectRepository.selectAll();
        assertThat(insertedTasksInProject).hasSize(tasks.size());
        for (Task task : tasks) {
            assertTrue(insertedTasksInProject.stream().anyMatch(t-> Objects.equals(t.getIdProject(),
                    project.getIdProject()) && Objects.equals(t.getIdTask(), task.getIdTask())));
        }
    }

    @Test
    void deleteTaskInProjectTest() {
        User user = generateDefaultUser();
        userDao.insert(user);

        Project project = generateDefaultProject(user);
        projectDao.insert(project);

        List<Task> tasks = new ArrayList<>(generateTasks(user.getIdUser()).toList());
        for (Task task : tasks) {
            taskDao.insert(task);
        }
        for(Task task : tasks) {
            taskInProjectRepository.insert(project.getIdProject(), task.getIdTask());
        }


        TaskInProject deletedTaskInProject = taskInProjectRepository.selectByTaskAndProject(project.getIdProject(),
                tasks.getFirst().getIdTask());
        taskInProjectRepository.delete(deletedTaskInProject.getIdTaskInProject());

        List<TaskInProject> insertedTasksInProject = taskInProjectRepository.selectAll();

        assertThat(insertedTasksInProject).hasSize(tasks.size() - 1);
        tasks.remove(tasks.getFirst());
        for (Task task : tasks) {
            assertTrue(insertedTasksInProject.stream().anyMatch(t-> Objects.equals(t.getIdProject(),
                    project.getIdProject()) && Objects.equals(t.getIdTask(), task.getIdTask())));
        }
    }


    @Test
    void selectTasksByProjectIdTest() {
        User user = generateDefaultUser();
        userDao.insert(user);

        Project project = generateDefaultProject(user);
        projectDao.insert(project);
        Project project2 = generateDefaultProject(user);
        project2.setName("project2");
        projectDao.insert(project2);

        List<Task> tasks = new ArrayList<>(generateTasks(user.getIdUser()).toList());
        for (Task task : tasks) {
            taskDao.insert(task);
        }
        for(Task task : tasks) {
            taskInProjectRepository.insert(project.getIdProject(), task.getIdTask());
            taskInProjectRepository.insert(project2.getIdProject(), task.getIdTask());
        }

        List<TaskInProject> insertedTasksInProject = taskInProjectRepository.selectByProjectId(project.getIdProject());
        assertThat(insertedTasksInProject).hasSize(tasks.size());
        for (Task task : tasks) {
            assertTrue(insertedTasksInProject.stream().anyMatch(t-> Objects.equals(t.getIdProject(),
                    project.getIdProject()) && Objects.equals(t.getIdTask(), task.getIdTask())));
        }

    }
    @Test
    void selectTaskByTaskAndProjectTest() {
        User user = generateDefaultUser();
        userDao.insert(user);

        Project project = generateDefaultProject(user);
        projectDao.insert(project);

        List<Task> tasks = new ArrayList<>(generateTasks(user.getIdUser()).toList());
        for (Task task : tasks) {
            taskDao.insert(task);
        }
        for(Task task : tasks) {
            taskInProjectRepository.insert(project.getIdProject(), task.getIdTask());
        }

        TaskInProject selectedTaskInProject = taskInProjectRepository
                .selectByTaskAndProject(tasks.getFirst().getIdTask(), project.getIdProject());
        assertEquals(selectedTaskInProject.getIdTask(), tasks.getFirst().getIdTask());
        assertEquals(selectedTaskInProject.getIdProject(), project.getIdProject());
    }
}
