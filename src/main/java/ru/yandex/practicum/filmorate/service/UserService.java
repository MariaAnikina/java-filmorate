package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import static ru.yandex.practicum.filmorate.service.validators.UserValidator.validate;

import java.util.*;

@Service
public class UserService {
    UserStorage userStorage;
    FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("databaseUser") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public void addAsFriend(Integer id, Integer friendId) {
        getUserById(id);
        getUserById(friendId);
        friendStorage.addFriend(id, friendId);
    }

    public void removeFromFriends(Integer id, Integer friendId) {
        getUserById(id);
        getUserById(friendId);
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> displayListOfMutualFriends(Integer id, Integer friendId) {
        getUserById(id);
        getUserById(friendId);
        return friendStorage.getCommonFriends(id, friendId);
    }

    public List<User> getListOfFriends(Integer id) {
        getUserById(id);
        return friendStorage.getFriends(id);
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.update(user);
    }
}
