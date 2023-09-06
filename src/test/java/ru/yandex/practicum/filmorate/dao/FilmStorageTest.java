package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

    @Qualifier("databaseUser")
    private UserStorage userStorage;

    @Test
    void shouldGetAll() {
        Film film1 = new Film("Оно", "Красный шарик", LocalDate.parse("2017-09-07"),
                135, new HashSet<>(), 0, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film1);
        Film film2 = new Film("Оно 2", "Красный шарик 2", LocalDate.parse("2017-12-07"),
                190, new HashSet<>(), 0, new RatingMPA(5, "NC-17"), new ArrayList<>());
        filmStorage.create(film2);
        List<Film> films = filmStorage.getFilms();
        assertEquals(2, films.size());
        assertEquals(film1, films.get(0));
        assertEquals(film2, films.get(1));
    }

    @Test
    void shouldAdd() {
        Film film = new Film("Оно", "Красный шарик", LocalDate.parse("2017-09-07"),
                135, new HashSet<>(), 0, new RatingMPA(1, "G"), new ArrayList<>());
        Film addedFilm = filmStorage.create(film);

        assertEquals(film, addedFilm);
    }

    @Test
    void shouldUpdate() {
        Film film = new Film("Оно", "Красный шарик", LocalDate.parse("2017-09-07"),
                135, new HashSet<>(), 0, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film);
        Film newFilm = new Film("Оно 2", "Красный шарик 2", LocalDate.parse("2017-12-07"),
                190, new HashSet<>(), 0, new RatingMPA(5, "NC-17"), new ArrayList<>());
        filmStorage.create(newFilm);
        newFilm.setId(film.getId());
        Film updatedFilm = filmStorage.update(newFilm);

        assertEquals(newFilm, updatedFilm);
    }

    @Test
    void shouldNotUpdateWhenIncorrectId() {
        FilmNotFoundException e = Assertions.assertThrows(
                FilmNotFoundException.class,
                () -> {
                    Film film = new Film("Оно", "Красный шарик", LocalDate.parse("2017-09-07"),
                            135, new HashSet<>(), 0, new RatingMPA(1, "G"), new ArrayList<>());
                    film.setId(100);
                    filmStorage.update(film);
                }
        );

        assertEquals("Фильм с id=100 не найден.", e.getMessage());
    }

    @Test
    void shouldGetFilmById() {
        Film film = new Film("Оно", "Красный шарик", LocalDate.parse("2017-09-07"),
                135, new HashSet<>(), 0, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film);

        Film filmById = filmStorage.findFilmById(film.getId());
        assertEquals(film, filmById);
    }
    @Test
    void shouldGetMostPopular(@Autowired LikeStorage likeStorage) {
        Film film1 = new Film("Оно", "Красный шарик", LocalDate.parse("2017-09-07"),
                135, new HashSet<>(), 0, new RatingMPA(1, "G"), new ArrayList<>());
        filmStorage.create(film1);
        Film film2 = new Film("Оно 2", "Красный шарик 2", LocalDate.parse("2017-12-07"),
                190, new HashSet<>(), 0, new RatingMPA(5, "NC-17"), new ArrayList<>());
        filmStorage.create(film2);
        User user = new User(1, "bot@mail.ru", "botLogin",
                "bot", LocalDate.parse("2000-02-20"), new HashSet<>());
        userStorage.create(user);
        likeStorage.addLike(film2.getId(), user.getId());
        List<Film> mostPopular = filmStorage.getMostPopular(10);

        assertEquals(film2, mostPopular.get(0));
        assertEquals(film1, mostPopular.get(1));
    }
}