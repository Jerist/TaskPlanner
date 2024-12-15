package ru.bulavin.datalayer.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.bulavin.datalayer.TestConnectionGetter;
import ru.bulavin.entity.User;
import ru.bulavin.repository.UserDao;
import ru.bulavin.repository.UserExistsDao;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

class UserExistsRepositoryTest {
    private final TestConnectionGetter connectionGetter = TestConnectionGetter.getInstance();
    private final UserExistsDao userExistsRepository = new UserExistsDao(connectionGetter);
    private final UserDao userRepository = new UserDao(connectionGetter);


    private static User defaultUser() {
        return User.builder()
                .name("defaultUser")
                .email("deafult@email.com")
                .phone("+71234567890")
                .password("defaultPassword")
                .salt("defaultUser")
                .build();
    }

    @SneakyThrows
    @AfterEach
    public void clear() {
        Connection connection = connectionGetter.get();
        //language=PostgreSQL
        String CLEAR_TABLE = "TRUNCATE taskplanner_test.public.user RESTART IDENTITY CASCADE";
        connection.prepareStatement(CLEAR_TABLE).executeUpdate();
    }

    @SneakyThrows
    @BeforeEach
    public  void insertUser() {
        User user = defaultUser();
        userRepository.insert(user);
    }

    @Test
    void existsByValidSomethingTest() {
        User user = defaultUser();

        assertThat(userExistsRepository.existsByEmail(user.getEmail())).isTrue();
        assertThat(userExistsRepository.existsByPhone(user.getPhone())).isTrue();
    }

    @Test
    void existsByInvalidSomethingTest() {
        assertThat(userExistsRepository.existsByEmail("wrongEmail")).isFalse();
        assertThat(userExistsRepository.existsByPhone("wrongPhoneNumber")).isFalse();
        assertThat(userExistsRepository.existsByEmail(null)).isFalse();
        assertThat(userExistsRepository.existsByPhone(null)).isFalse();
    }
}
