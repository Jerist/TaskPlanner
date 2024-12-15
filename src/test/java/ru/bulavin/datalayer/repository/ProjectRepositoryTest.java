package ru.bulavin.datalayer.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.bulavin.datalayer.TestConnectionGetter;
import ru.bulavin.entity.Project;
import ru.bulavin.entity.User;
import ru.bulavin.exception.DaoException;
import ru.bulavin.repository.ProjectDao;
import ru.bulavin.repository.UserDao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectRepositoryTest {
    private final TestConnectionGetter connectionGetter = TestConnectionGetter.getInstance();
    private final UserDao userDao = new UserDao(connectionGetter);
    private final ProjectDao projectRepository = new ProjectDao(connectionGetter, userDao);

    private static Stream<Project> generateInvalidProjects() {
        Project project1 = Project.builder()
                .name(null)
                .description("Description 1")
                .idUser(1L)
                .build();

        return Stream.of(project1);
    }
    private static Stream<Project> generateValidProjects() {
        return generateValidProjects(1L);
    }

    private static Stream<Project> generateValidProjects(Long idUser) {
        Project project1 = Project.builder()
                .name("Project 1")
                .description("Description 1")
                .idUser(idUser)
                .build();

        Project project2 = Project.builder()
                .name("Project 2")
                .description("Description 2")
                .idUser(idUser)
                .build();

        Project project3 = Project.builder()
                .name("Project 3")
                .description("Description 3")
                .idUser(idUser)
                .build();

        return Stream.of(project1, project2, project3);
    }


    private static User generateDefaultUser() {
        return User.builder()
                .name("user1")
                .email("user1@email.com")
                .phone("+71234567890")
                .password("123")
                .salt("salt1")
                .build();
    }

    @SneakyThrows
    @BeforeEach
    public void clear() {
        try (Connection connection = connectionGetter.get()) {
            //language=PostgreSQL
            String CLEAR_TABLE = "TRUNCATE taskplanner_test.public.project RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE).executeUpdate();
            //language=PostgreSQL
            String CLEAR_TABLE_USERS = "TRUNCATE taskplanner_test.public.user RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE_USERS).executeUpdate();
        }
    }

    @ParameterizedTest
    @MethodSource("generateInvalidProjects")
    void insertInvalidProjectTest(Project project) {
        assertThatThrownBy(() -> projectRepository.insert(project)).isInstanceOf(DaoException.class);
    }
    @ParameterizedTest
    @MethodSource("generateValidProjects")
    void insertValidProjectTest(Project project) {
        User user = generateDefaultUser();
        userDao.insert(user);
        project.setIdUser(user.getIdUser());
        assertThat(projectRepository.insert(project)).isTrue();

        Project insertedProject = projectRepository.selectById(project.getIdProject());
        assertEquals(project, insertedProject);
    }

    @Test
    void selectAllProjectsTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        List<Project> projects = generateValidProjects(user.getIdUser()).toList();
        for(Project project : projects) {
            projectRepository.insert(project);
        }
        List<Project> insertedProjects = projectRepository.selectAll();
        assertThat(insertedProjects).hasSize(projects.size());
        for(Project project : projects) {
            assertTrue(insertedProjects.contains(project));
        }
    }

    @Test
    void deleteProjectTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        List<Project> projects = new ArrayList<>(generateValidProjects(user.getIdUser()).toList());
        for (Project project : projects) {
            projectRepository.insert(project);
        }
        Project deletedProject = projects.getFirst();
        projectRepository.delete(deletedProject.getIdProject());
        List<Project> insertedProjects = projectRepository.selectAll();
        assertFalse(insertedProjects.contains(deletedProject));

        assertThat(insertedProjects).hasSize(projects.size()-1);

        projects.remove(deletedProject);
        for(Project project : projects) {
            assertTrue(insertedProjects.contains(project));
        }
    }

    @Test
    void updateProjectTest() {
        User user = generateDefaultUser();
        userDao.insert(user);
        List<Project> projects = new ArrayList<>(generateValidProjects(user.getIdUser()).toList());
        for(Project project : projects) {
            projectRepository.insert(project);
        }

        Project oldProject = projects.getFirst();
        Project updatedProject = Project.builder()
                .idProject(oldProject.getIdProject())
                .name("new name")
                .description(null)
                .idUser(oldProject.getIdUser())
                .build();
        assertEquals(1, projectRepository.update(updatedProject));
        projects.set(projects.indexOf(oldProject), updatedProject);
        List<Project> insertedProject = projectRepository.selectAll();
        assertThat(insertedProject).hasSize(projects.size());
        for(Project project : projects) {
            assertTrue(insertedProject.contains(project));
        }
    }

    @Test
    void selectProjectsByUserIdTest() {
        User user = generateDefaultUser();
        User user2 = User.builder()
                .name("user2")
                .email("user2@email.com")
                .phone("+70987654321")
                .password("qwerty")
                .salt("salt2")
                .build();

        userDao.insert(user);
        List<Project> projects = new ArrayList<>(generateValidProjects(user.getIdUser()).toList());
        for(Project project : projects) {
            projectRepository.insert(project);
        }

        userDao.insert(user2);
        List<Project> projects2 = new ArrayList<>(generateValidProjects(user2.getIdUser()).toList());
        for(Project project : projects2) {
            projectRepository.insert(project);
        }

        List<Project> insertedProjects = projectRepository.selectAllByUserId(user.getIdUser());

        assertThat(projects).hasSize(projects.size());
        for(Project project : projects) {
            assertTrue(insertedProjects.contains(project));
        }
    }
}
