package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    @Qualifier("databaseUser")
    private final UserStorage storage;

    @Test
    void shouldGetAll() {
        User user1 = new User(1, "cat1@mail.ru", "catttt",
                "Barsik", LocalDate.parse("2004-04-24"), new HashSet<>());
        storage.create(user1);
        User user2 = new User(2, "user@email.com", "user",
                "Oleg", LocalDate.parse("1994-05-12"), new HashSet<>());
        storage.create(user2);

        List<User> users = storage.getUsers();
        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
    }

    @Test
    void shouldAdd() {
        User user = new User(1, "cat1@mail.ru", "catttt",
                "Barsik", LocalDate.parse("2004-04-24"), new HashSet<>());
        User addedUser = storage.create(user);

        assertEquals(user, addedUser);
    }

    @Test
    void shouldUpdate() {
        User user = new User(1, "cat1@mail.ru", "catttt",
                "Barsik", LocalDate.parse("2004-04-24"), new HashSet<>());
        User addedUser = storage.create(user);
        User newUser = new User(1, "user@email.com", "user",
                "Oleg", LocalDate.parse("1994-05-12"), new HashSet<>());
        User updatedUser = storage.update(newUser);

        assertEquals(newUser, updatedUser);
    }

    @Test
    void shouldNotUpdateWhenIncorrectId() {
        UserNotFoundException e = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> {
                    User user = new User(100, "user@email.com", "user",
                            "Oleg", LocalDate.parse("1994-05-12"), new HashSet<>());
                    storage.update(user);
                }
        );

        assertEquals("Пользователь с id=100 не найден.", e.getMessage());
    }

    @Test
    void shouldGetUserById() {
        User user = new User(2, "user@email.com", "user",
                "Oleg", LocalDate.parse("1994-05-12"), new HashSet<>());
        User addedUser = storage.create(user);

        User userById = storage.getUserById(user.getId());
        assertEquals(user, userById);
    }
}