package ru.yandex.practicum.filmorate;

import org.springframework.web.bind.annotation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController;
    @BeforeEach
    void createFilmController() {
        filmController = new FilmController();
    }

    @Test
    void createTheRightMovie() {
        Film film = Film.builder()
                .name("Оно")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film);
        assertEquals(1, filmController.getFilms().size());
        assertEquals(1, film.getId());
    }

    @Test
    void createMovieWithEmptyName() {
        Film film = Film.builder()
                .name(" ")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertEquals("Параметры фильма не соответствуют требованиям.", exception.getMessage());
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void creatingMovieWithDescriptionOfMoreThan20Characters() {
        Film film = Film.builder()
                .name("Оно")
                .description("Когда в городке Дерри, штат Мэн, начинают пропадать дети, несколько ребят сталкиваются" +
                        "со своими величайшими страхами и вынуждены помериться силами со злобным клоуном Пеннивайзом," +
                        " чьи проявления жестокости и список жертв уходят в глубь веков.")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertEquals("Параметры фильма не соответствуют требованиям.", exception.getMessage());
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void creatingMovieWithAnIncorrectCreationDate() {
        Film film = Film.builder()
                .name("Оно")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(1017, 9, 7))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertEquals("Параметры фильма не соответствуют требованиям.", exception.getMessage());
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void creatingMovieWithNegativeDuration() {
        Film film = Film.builder()
                .name("Оно")
                .description("Ужасы")
                .duration(-1)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertEquals("Параметры фильма не соответствуют требованиям.", exception.getMessage());
        assertEquals(0, filmController.getFilms().size());
    }

    @Test
    void creatingAnExistingMovie() {
        Film film = Film.builder()
                .name("Оно")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film);
        Film filmClone = Film.builder()
                .id(1)
                .name("Оно")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.createFilm(filmClone));
        assertEquals("Такой фильм уже существует.", exception.getMessage());
        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void updatingAnExistingMovie() {
        Film film = Film.builder()
                .name("Оно")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film);
        Film filmUpdate = Film.builder()
                .id(1)
                .name("Оно")
                .description("Ужасы")
                .duration(136)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.updateFilm(filmUpdate);
        assertEquals(1, filmController.getFilms().size());
        assertEquals(136, filmController.getFilms().get(0).getDuration());
    }

    @Test
    void updatingDefunctMovie() {
        Film film = Film.builder()
                .name("Оно")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film);
        Film filmUpdate = Film.builder()
                .id(2)
                .name("Оно")
                .description("Ужасы")
                .duration(136)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(filmUpdate));
        assertEquals("Такого фильма еще не существует.", exception.getMessage());
        assertEquals(1, filmController.getFilms().size());
    }
}
