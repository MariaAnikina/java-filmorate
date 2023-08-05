package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id) {
        if (filmStorage.getFilms().containsKey(id)) {
            return filmStorage.getFilms().get(id);
        }
        throw new NullPointerException();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Set<Integer> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> displayPopularMovies(@RequestParam(defaultValue = "10") int count) {
        return filmService.displayPopularMovies(count);
    }
}
