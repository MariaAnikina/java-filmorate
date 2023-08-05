package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Set<Integer> addAsFriend(Integer id, Integer friendId) {
        User user1 = userStorage.getUsers().get(id);
        User user2 = userStorage.getUsers().get(friendId);
        if (user1.getFriends() == null) {
            user1.setFriends(new HashSet<>());
        }
        if (user2.getFriends() == null) {
            user2.setFriends(new HashSet<>());
        }
        user1.getFriends().add(friendId);
        user2.getFriends().add(id);
        return user1.getFriends();
    }

    public Set<Integer> removeFromFriends(Integer id, Integer friendId) {
        User user1 = userStorage.getUsers().get(id);
        User user2 = userStorage.getUsers().get(friendId);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
        return user1.getFriends();
    }

    public Set<User> displayListOfMutualFriends(Integer id, Integer friendId) {
        User user1 = userStorage.getUsers().get(id);
        User user2 = userStorage.getUsers().get(friendId);
        Set<User> mutualFriends = new HashSet<>();
        if (user1.getFriends() != null) {
            for (Integer idUser : user1.getFriends()) {
                if (user2.getFriends().contains(idUser)) {
                    mutualFriends.add(userStorage.getUsers().get(idUser));
                }
            }
        }
        return mutualFriends;
    }

    public List<User> getListOfFriends(Integer id) {
        User user = userStorage.getUsers().get(id);
        List<User> friends = new ArrayList<>();
        if (user.getFriends() != null) {
            for (Integer friend : user.getFriends()) {
                friends.add(userStorage.getUsers().get(friend));
            }
        }
        return friends;
    }
}
