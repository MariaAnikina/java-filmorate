package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private int idFilm = 1;

    @Override
    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            log.warn("Возникло исключение - попытка создать существующий фильм: {}", film);
            throw new ValidationException("Такой фильм уже существует.");
        }
        if (film.getName().isBlank()) {
            log.warn("Возникло исключение - попытка создать фильм без названия: {}", film);
            throw new ValidationException("У фильма должно быть название.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Возникло исключение - попытка создать фильм с описанием длиною более 200 символов: {}", film);
            throw new ValidationException("Описание бильма должно быть длигною менее 201 символа");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Возникло исключение - попытка создать фильм с неккоректной датой выпуска: {}", film);
            throw new ValidationException("В то время фильмов не было(");
        }
        if (film.getDuration() <= 0) {
            log.warn("Возникло исключение - попытка создать фильм с неккоректной длительностью: {}", film);
            throw new ValidationException("Длительность фильма должна быть положительна");
        }
        if (film.getId() == null) {
            film.setId(idFilm);
            idFilm++;
        }
        films.put(film.getId(), film);
        log.debug("Логирование созданного объекта: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Возникло исключение - попытка создать несуществующий фильм: {}", film);
            throw new NullPointerException("Такого фильма еще не существует.");
        }
        if (film.getName().isBlank()) {
            log.warn("Возникло исключение - попытка создать фильм без названия: {}", film);
            throw new ValidationException("У фильма должно быть название.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Возникло исключение - попытка создать фильм с описанием длиною более 200 символов: {}", film);
            throw new ValidationException("Описание бильма должно быть длигною менее 201 символа");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Возникло исключение - попытка создать фильм с неккоректной датой выпуска: {}", film);
            throw new ValidationException("В то время фильмов не было(");
        }
        if (film.getDuration() <= 0) {
            log.warn("Возникло исключение - попытка создать фильм с неккоректной длительностью: {}", film);
            throw new ValidationException("Длительность фильма должна быть положительна");
        }
        if (film.getId() == null) {
            film.setId(idFilm);
            idFilm++;
        }
        films.put(film.getId(), film);
        log.debug("Логирование обнавленного объекта: {}", film);
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(int id) {
        return null;
    }

    @Override
    public List<Film> getMostPopular(int size) {
        return null;
    }

    public void setFilms(Map<Integer, Film> films) {
        this.films = films;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }
}
