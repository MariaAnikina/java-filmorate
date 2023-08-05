package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int idUser = 1;

    @Override
    public User createUser(User user) {
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
        log.debug("Логирование созданного объекта: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
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
        if (!users.containsKey(user.getId())) {
            log.warn("Возникло исключение - попытка создать несуществующего пользователя: {}", user);
            throw new NullPointerException("Пользователь еще не существует.");
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

    @Override
    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            log.debug("Логирование удаленного объекта: {}", user);
            return user;
        }
        throw new ValidationException("Пользователя с id = " + user.getId() + " не существует");
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public void setUsers(Map<Integer, User> users) {
        this.users = users;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
