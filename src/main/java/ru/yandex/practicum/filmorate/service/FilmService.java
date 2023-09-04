package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.service.validators.FilmValidator.validate;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private GenreStorage genreStorage;
    private LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage")FilmStorage filmStorage, @Qualifier("databaseUser") UserStorage userStorage,
                       GenreStorage genreStorage,  LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }

    public void addLike(Integer id, Integer userId) {
        filmStorage.findFilmById(id);
        userStorage.getUserById(userId);
        likeStorage.addLike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        filmStorage.findFilmById(id);
        userStorage.getUserById(userId);
        likeStorage.deleteLike(id, userId);
    }

    public List<Film> displayPopularMovies(int count) {
        List<Film> films = filmStorage.getMostPopular(count);
        films.forEach(film -> film.setGenres(genreStorage.getFilmGenres(film.getId())));
        return films;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        films.forEach(film -> film.setGenres(genreStorage.getFilmGenres(film.getId())));
        return films;
    }

    public Film create(Film film) {
        validate(film);
        return genreStorage.addFilmGenres(filmStorage.create(film));
    }

    public Film update(Film film) {
        validate(film);
        genreStorage.deleteFilmGenres(film.getId());
        return genreStorage.addFilmGenres(filmStorage.update(film));
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.findFilmById(id);
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        return film;
    }
}
