package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("memoryUser")
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int idUser = 1;

    @Override
    public User create(User user) {
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
    public User update(User user) {
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

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        return null;
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
