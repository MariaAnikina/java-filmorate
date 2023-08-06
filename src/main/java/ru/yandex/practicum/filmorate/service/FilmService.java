package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Set<Integer> addLike(Integer id, Integer userId) {
        Film film = filmStorage.getFilms().get(id);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        film.getLikes().add(userId);
        if (film.getRate() != null) {
            film.setRate(film.getRate() + 1);
        } else {
            film.setRate(1);
        }
        return film.getLikes();
    }

    public Film deleteLike(Integer id, Integer userId) {
        Film film = filmStorage.getFilms().get(id);
        User user = userStorage.getUsers().get(userId);
        film.getLikes().remove(user.getId());
        film.setRate(film.getRate() - 1);
        return film;
    }

    public List<Film> displayPopularMovies(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(Film::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}
