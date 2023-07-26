package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int idUser = 1;

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Возникло исключение - попытка создать пользователя с неккоректным Email: {}", user);
            throw new ValidationException("Email должен содержать символ - @, а также не может быть пустым");
        }
        if (user.getLogin().isBlank() ||  user.getLogin().contains(" ")) {
            log.warn("Возникло исключение - попытка создать пользователя с неккоректным логином: {}", user);
            throw new ValidationException("Login не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Возникло исключение - попытка создать пользователя с неккоректной датой рождения: {}", user);
            throw new ValidationException("Неккоректная дата рождения");
        }
        if (user.getId() == null) {
            user.setId(idUser);
            idUser++;
        }
        if (users.containsKey(user.getId())) {
            log.warn("Возникло исключение - попытка создать существующего пользователя: {}", user);
            throw new ValidationException("Пользователь уже существует.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("Логирование обнавленного объекта: {}", user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Возникло исключение - попытка обновить несуществующего пользователя: {}", user);
            throw new ValidationException("Такого пользователя еще не существует.");
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@") || user.getLogin().isBlank() ||
                user.getBirthday().isAfter(LocalDate.now()) || user.getLogin().contains(" ")) {
            log.warn("Возникло исключение - попытка обновить пользователя с неккоректными параметрами: {}", user);
            throw new ValidationException("Параметры пользователя не соответствуют требованиям.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("Логирование обнавленного объекта: {}", user);
        return user;
    }
}
