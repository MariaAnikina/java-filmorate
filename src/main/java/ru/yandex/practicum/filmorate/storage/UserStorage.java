package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User delete(User user);

    Map<Integer, User> getUsers();
}
