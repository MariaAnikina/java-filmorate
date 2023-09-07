package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping(value = "/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addAsFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addAsFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Integer id, @PathVariable  Integer friendId) {
       userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id) {
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> displayListOfMutualFriends(@PathVariable Integer id, @PathVariable  Integer otherId) {
        return userService.displayListOfMutualFriends(id, otherId);
    }
}
