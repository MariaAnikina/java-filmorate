package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();
    private int idFilm = 1;
    @GetMapping ("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping (value = "/films")
    public Film createFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            film.setId(idFilm);
            idFilm++;
        }
        if (films.containsKey(film.getId())) {
            log.warn("Возникло исключение - попытка создать существующий фильм: {}", film);
            throw new ValidationException("Такой фильм уже существует.");
        }
        if (film.getName().isBlank() || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getDuration() <= 0) {
            log.warn("Возникло исключение - попытка создать фильм с неккоректными параметрами: {}", film);
            throw new ValidationException("Параметры фильма не соответствуют требованиям.");
        }
        films.put(film.getId(), film);
        log.debug("Логирование созданного объекта: {}", film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Возникло исключение - попытка обновить несуществующий фильм: {}", film);
            throw new ValidationException("Такого фильма еще не существует.");
        }
        if (film.getName().isBlank()|| film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getDuration() <= 0) {
            log.warn("Возникло исключение - попытка обновить фильм с неккоректными параметрами: {}", film);
            throw new ValidationException("Параметры фильма не соответствуют требованиям.");
        }
        films.put(film.getId(), film);
        log.debug("Логирование обнавленного объекта: {}", film);
        return film;
    }
}
