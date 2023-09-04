package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user) throws UserNotFoundException;

    List<User> getUsers();

    User getUserById(int id);
}
