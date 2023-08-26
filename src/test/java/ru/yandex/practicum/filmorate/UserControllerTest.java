package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;
    private UserStorage userStorage;
    private UserService userService;

    @BeforeEach
    void createController() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void creatingCorrectUser() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.create(user);
        assertEquals(1, userStorage.getUsers().size());
        assertEquals(1, userStorage.getUsers().get(1).getId());
    }

    @Test
    void creatingAnUnnamedUser() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name(" ")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.create(user);
        assertEquals(1, userStorage.getUsers().size());
        assertEquals("Мур", user.getName());
        assertEquals(1, user.getId());
    }

    @Test
    void createWithoutEmail() {
        User user = User.builder()
                .email(" ")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Email должен содержать символ - @, а также не может быть пустым", exception.getMessage());
    }

    @Test
    void creatingUserWithIncorrectEmail() {
        User user = User.builder()
                .email("catmail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Email должен содержать символ - @, а также не может быть пустым", exception.getMessage());
    }

    @Test
    void creatingUserWithIncorrectLogin() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Login не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void creatingUserWithAnEmptyLogin() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login(" ")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Login не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void creatingUserWithIncorrectDateOfBirth() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2025, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Неккоректная дата рождения", exception.getMessage());
    }

    @Test
    void creatingAnExistingUser() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.create(user);
        User userClone = User.builder()
                .id(1)
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(userClone));
        assertEquals("Пользователь уже существует.", exception.getMessage());
        assertEquals(1, userStorage.getUsers().size());
    }

    @Test
    void updatingAnExistingUser() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.create(user);
        User userUpdate = User.builder()
                .id(1)
                .email("cat@mail.ru")
                .login("Мурка")
                .name("")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.update(userUpdate);
        assertEquals(1, userStorage.getUsers().size());
        assertEquals(1, user.getId());
        assertEquals("Мурка", userStorage.getUsers().get(1).getLogin());
        assertEquals("Мурка", userStorage.getUsers().get(1).getName());
    }

    @Test
    void updatingNonExistentUser() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.create(user);
        User userUpdate = User.builder()
                .id(10)
                .email("cat@mail.ru")
                .login("Мурка")
                .name("")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        NullPointerException exception = assertThrows(NullPointerException.class, () -> userController.update(userUpdate));
        assertEquals("Пользователь еще не существует.", exception.getMessage());
    }

    @Test
    void addingToFriends() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2021, 5, 3))
                .build();
        userController.create(user);
        User user2 = User.builder()
                .email("cat@mail.ru")
                .login("Test")
                .name("Friend")
                .birthday(LocalDate.of(2022, 5, 3))
                .build();
        userController.create(user2);
        userController.addAsFriend(user.getId(), user2.getId());
        assertEquals(true, user2.getFriends().contains(1));
        assertEquals(true, user.getFriends().contains(2));
    }
}
