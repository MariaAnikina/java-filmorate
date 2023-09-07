package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "merge into MOVIE_LIKE (film_id, user_id) key (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Фильм с id={} получил лайк от пользователя с id={}.", filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update("delete from MOVIE_LIKE where film_id = ? and user_id = ?", filmId, userId);
        log.info("Удален лайк фильма с id={} от пользователя с id={}.", filmId, userId);
    }

    @Override
    public List<Integer> getLikes(int filmId) {
        String sqlQuery = "select user_id from MOVIE_LIKE where film_id = ?";
        return jdbcTemplate.query(
                sqlQuery,
                (rs, rowNum) -> rs.getInt("user_id"),
                filmId);
    }
}