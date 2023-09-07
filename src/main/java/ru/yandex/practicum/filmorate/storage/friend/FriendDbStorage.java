package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.FilmorateMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "merge into friend (user_id, friend_id) key (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь с id={} добавил в друзья пользователя с id={}.", userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from friend where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Пользователь с id={} удалил из друзей пользователя с id={}.", userId, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        return jdbcTemplate.query("select users.* from users join friend on users.user_id = friend.friend_id " +
                "where friend.user_id = ?", FilmorateMapper::userFromRow, id);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        return jdbcTemplate.query("select users.* from users join friend on users.user_id = friend.friend_id " +
                        "where friend.user_id in (?, ?) group by users.user_id having count (friend.friend_id) > 1",
                FilmorateMapper::userFromRow, id, otherId);
    }
}