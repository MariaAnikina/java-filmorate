package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    UserController userController;

    @BeforeEach
    void createUserController() {
        userController = new UserController();
    }

    @Test
    void creatingCorrectUser(){
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.createUser(user);
        assertEquals(1, userController.getUsers().size());
        assertEquals(1, user.getId());
    }

    @Test
    void creatingAnUnnamedUser() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name(" ")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.createUser(user);
        assertEquals(1, userController.getUsers().size());
        assertEquals("Мур", user.getName());
        assertEquals(1, user.getId());
    }

    @Test
    void createUserWithoutEmail() {
        User user = User.builder()
                .email(" ")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Параметры пользователя не соответствуют требованиям.", exception.getMessage());
    }

    @Test
    void creatingUserWithIncorrectEmail() {
        User user = User.builder()
                .email("catmail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Параметры пользователя не соответствуют требованиям.", exception.getMessage());
    }

    @Test
    void creatingUserWithIncorrectLogin() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Параметры пользователя не соответствуют требованиям.", exception.getMessage());
    }

    @Test
    void creatingUserWithAnEmptyLogin() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login(" ")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Параметры пользователя не соответствуют требованиям.", exception.getMessage());
    }

    @Test
    void creatingUserWithIncorrectDateOfBirth() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2025, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Параметры пользователя не соответствуют требованиям.", exception.getMessage());
    }

    @Test
    void creatingAnExistingUser() {
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.createUser(user);
        User userClone = User.builder()
                .id(1)
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(userClone));
        assertEquals("Пользователь уже существует.", exception.getMessage());
        assertEquals(1, userController.getUsers().size());
    }
    @Test
    void updatingAnExistingUser(){
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.createUser(user);
        User userUpdate = User.builder()
                .id(1)
                .email("cat@mail.ru")
                .login("Мурка")
                .name("")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.updateUser(userUpdate);
        assertEquals(1, userController.getUsers().size());
        assertEquals(1, user.getId());
        assertEquals("Мурка", userController.getUsers().get(0).getLogin());
        assertEquals("Мурка", userController.getUsers().get(0).getName());
    }

    @Test
    void updatingNonExistentUser(){
        User user = User.builder()
                .email("cat@mail.ru")
                .login("Мур")
                .name("Барсик")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        userController.createUser(user);
        User userUpdate = User.builder()
                .id(10)
                .email("cat@mail.ru")
                .login("Мурка")
                .name("")
                .birthday(LocalDate.of(2020, 5, 3))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.updateUser(userUpdate));
        assertEquals("Такого пользователя еще не существует.", exception.getMessage());
    }
}
