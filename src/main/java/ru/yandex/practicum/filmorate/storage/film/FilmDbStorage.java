package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmorateMapper;

import java.util.List;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "select f.*, rating.name as mpa_name from film as f left join rating on " +
                "f.mpa_id = rating.id";
        return jdbcTemplate.query(sqlQuery, FilmorateMapper::filmFromRow);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(FilmorateMapper.filmToRow(film)).intValue());
        log.info("Добавлен новый фильм {}.", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update film set name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? where film_id = ?";
        int filmId = film.getId();
        int rowsUpdated = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                filmId
        );
        if (rowsUpdated == 1) {
            log.info("Обновлен фильм {}.", film);
            return film;
        } else {
            throw new FilmNotFoundException("Фильм с id=" + filmId + " не найден.");
        }
    }

    @Override
    public Film findFilmById(int id) {
        String sqlQuery = "select f.*, rating.name as mpa_name from film as f left join rating " +
                "on f.mpa_id = rating.id where f.film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, FilmorateMapper::filmFromRow, id);
    }

    @Override
    public List<Film> getMostPopular(int size) {
        String sqlQuery = "select f.*, rating.name as mpa_name from film as f left join rating on f.mpa_id " +
                "= rating.id left join movie_like on f.film_id = movie_like.film_id group by movie_like.film_id," +
                " f.film_id order by count (movie_like.user_id) desc, movie_like.film_id limit ?";
        return jdbcTemplate.query(sqlQuery, FilmorateMapper::filmFromRow, size);
    }
}