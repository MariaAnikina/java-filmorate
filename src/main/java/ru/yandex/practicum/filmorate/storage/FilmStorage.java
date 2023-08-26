package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film delete(Film film);

    Map<Integer, Film> getFilms();
}
