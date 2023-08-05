package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RestController
@Slf4j
public class UserController {
    private UserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Integer id) {
        if (userStorage.getUsers().containsKey(id)) {
            return userStorage.getUsers().get(id);
        }
        throw new NullPointerException();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public Set<Integer> addAsFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addAsFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public Set<Integer> removeFromFriends(@PathVariable Integer id, @PathVariable  Integer friendId) {
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id) {
        return userService.getListOfFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> displayListOfMutualFriends(@PathVariable Integer id, @PathVariable  Integer otherId) {
        return userService.displayListOfMutualFriends(id, otherId);
    }
}
