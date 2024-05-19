package ru.bulavin.datalayer.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.bulavin.datalayer.TestConnectionGetter;
import ru.bulavin.dto.user.controller.UserPasswordAndSaltControllerDto;
import ru.bulavin.entity.User;
import ru.bulavin.exception.DaoException;
import ru.bulavin.repository.UserDao;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private final TestConnectionGetter connectionGetter = TestConnectionGetter.getInstance();
    private final UserDao userRepository = new UserDao(connectionGetter);

    private static Stream<User> generateInvalidUsers() {
        User user1 = User.builder()
                .name(null)
                .email("user1@email.com")
                .phone("+71234567890")
                .password("user1")
                .salt("abc")
                .build();

        User user2 = User.builder()
                .name("user2")
                .email(null)
                .phone("+70987654321")
                .password("user2")
                .salt("abc")
                .build();

        User user3 = User.builder()
                .name("user3")
                .email("user3@email.com")
                .phone(null)
                .password("user3")
                .salt("abc")
                .build();

        User user4 = User.builder()
                .name("user4")
                .email("user4@email.com")
                .phone("+71233211231")
                .password(null)
                .salt("salt4")
                .build();


        User user5 = User.builder()
                .name("user4")
                .email("user4@email.com")
                .phone("+71233211231")
                .password("user5")
                .salt(null)
                .build();

        return Stream.of(user1, user2, user3, user4, user5);
    }

    private static Stream<User> generateValidUser() {
        User user1 = User.builder()
                .name("user1")
                .email("user1@email.com")
                .phone("+71234567890")
                .password("123")
                .salt("salt1")
                .build();

        User user2 = User.builder()
                .name("user2")
                .email("user2@email.com")
                .phone("+70987654321")
                .password("qwerty")
                .salt("salt2")
                .build();

        User user3 = User.builder()
                .name("user3")
                .email("user3@email.com")
                .phone("+71212312312")
                .password("abcd")
                .salt("salt3")
                .build();
        return Stream.of(user1, user2, user3);
    }


    @SneakyThrows
    @BeforeEach
    public void clear() {
        try (Connection connection = connectionGetter.get()) {
            //language=PostgreSQL
            String CLEAR_TABLE = "TRUNCATE taskplanner_test.public.user RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE).executeUpdate();
        }
    }

    @ParameterizedTest
    @MethodSource("generateInvalidUsers")
    void insertInvalidUserTest(User user) {
        assertThatThrownBy(() -> userRepository.insert(user)).isInstanceOf(DaoException.class);
    }

    @ParameterizedTest
    @MethodSource("generateValidUser")
    void insertValidUserTest(User user) {
        assertThat(userRepository.insert(user)).isTrue();
        assertThat(user.getIdUser()).isNotZero();

        User insertedUser = userRepository.selectById(user.getIdUser());
        assertEquals(user.getName(), insertedUser.getName());
        assertEquals(user.getEmail(), insertedUser.getEmail());
        assertEquals(user.getPhone(), insertedUser.getPhone());
        assertEquals(user.getSalt(), insertedUser.getSalt());
        assertEquals(user.getPassword(), insertedUser.getPassword());
    }
    @Test
    void selectAllUsersTest() {
        List<User> users = generateValidUser().toList();
        for(User user : users) {
            userRepository.insert(user);
        }

        List<User> insertedUsers = userRepository.selectAll();
        assertThat(insertedUsers).hasSize(users.size());
        for(User user: users) {
            assertTrue(insertedUsers.contains(user));
        }
    }
    @Test
    void deleteUserTest() {
        List<User> users = new ArrayList<>(generateValidUser().toList());
        for(User user : users) {
            userRepository.insert(user);
        }
        User removedUser = users.getFirst();
        assertEquals(1, userRepository.delete(removedUser.getIdUser()));

        List<User> insertedUsers = userRepository.selectAll();
        assertFalse(insertedUsers.contains(removedUser));
        assertThat(insertedUsers).hasSize(users.size() - 1);

        users.remove(removedUser);
        for(User user : users) {
            assertTrue(insertedUsers.contains(user));
        }
    }

    @Test
    void updateUserTest() {
        List<User> users = new ArrayList<>(generateValidUser().toList());
        for(User user : users) {
            userRepository.insert(user);
        }
        User oldUser = users.getFirst();
        User updatableUser = User.builder()
                .idUser(oldUser.getIdUser())
                .name("new user")
                .email("new_user@email.com")
                .phone("+71234567812")
                .salt("new user")
                .password("new user password")
                .build();
        assertEquals(1, userRepository.update(updatableUser));

        List<User> insertedUsers = userRepository.selectAll();
        assertFalse(insertedUsers.contains(oldUser));
        assertThat(insertedUsers).hasSize(users.size());

        users.set(users.indexOf(oldUser), updatableUser);
        for(User user : users) {
            assertTrue(insertedUsers.contains(user));
        }
    }

    @Test
    void selectByLoginTest() {
        List<User> users = generateValidUser().toList();
        for(User user : users) {
            userRepository.insert(user);
        }

        User user = users.getFirst();

        User selectedUser = userRepository.selectByLogin(user.getPhone());
        assertEquals(selectedUser, user);
    }

    @Test
    void selectPasswordByLoginTest() {
        List<User> users = generateValidUser().toList();
        for(User user : users) {
            userRepository.insert(user);
        }

        User user = users.getFirst();

        UserPasswordAndSaltControllerDto passwordAndSalt = userRepository.selectPasswordByLogin(user.getPhone());

        assertEquals(user.getPassword(), passwordAndSalt.password().orElse(null));
        assertEquals(user.getSalt(), passwordAndSalt.salt().orElse(null));
    }
}
