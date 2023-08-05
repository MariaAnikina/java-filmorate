package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface FilmStorage {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    Film deleteFilm(Film film);
    Map<Integer, Film> getFilms();
}
