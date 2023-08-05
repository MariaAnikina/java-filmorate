package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    private FilmController filmController;
    private FilmService filmService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void createFilmController() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
        userService = new UserService(userStorage);
        filmController = new FilmController(filmStorage, filmService);
        userController = new UserController(userStorage, userService);
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
        assertEquals(1, filmStorage.getFilms().size());
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
        assertEquals("У фильма должно быть название.", exception.getMessage());
        assertEquals(0, filmStorage.getFilms().size());
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
        assertEquals("Описание бильма должно быть длигною менее 201 символа", exception.getMessage());
        assertEquals(0,filmStorage.getFilms().size());
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
        assertEquals("В то время фильмов не было(", exception.getMessage());
        assertEquals(0, filmStorage.getFilms().size());
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
        assertEquals("Длительность фильма должна быть положительна", exception.getMessage());
        assertEquals(0, filmStorage.getFilms().size());
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
        assertEquals(1, filmStorage.getFilms().size());
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
        assertEquals(1, filmStorage.getFilms().size());
        assertEquals(136, filmStorage.getFilms().get(1).getDuration());
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
        NullPointerException exception = assertThrows(NullPointerException.class, () -> filmStorage.updateFilm(filmUpdate));
        assertEquals("Такого фильма еще не существует.", exception.getMessage());
        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    void getListOfPopularMovies() {
        Film film1 = Film.builder()
                .name("Оно1")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film1);
        Film film2 = Film.builder()
                .name("Оно2")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film2);
        Film film3 = Film.builder()
                .name("Оно3")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film3);
        Film film4 = Film.builder()
                .name("Оно4")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film4);
        Film film5 = Film.builder()
                .name("Оно5")
                .description("Ужасы")
                .duration(135)
                .releaseDate(LocalDate.of(2017, 9, 7))
                .build();
        filmController.createFilm(film5);
        User user1 = User.builder()
                .email("cat1@mail.ru")
                .login("Мур1")
                .name("Барсик")
                .birthday(LocalDate.of(2021, 5, 3))
                .build();
        userController.createUser(user1);
        User user2 = User.builder()
                .email("cat2@mail.ru")
                .login("Мур2")
                .name("Барсик")
                .birthday(LocalDate.of(2022, 5, 3))
                .build();
        userController.createUser(user2);
        User user3 = User.builder()
                .email("cat3@mail.ru")
                .login("Мур3")
                .name("Барсик")
                .birthday(LocalDate.of(2022, 5, 3))
                .build();
        userController.createUser(user3);
        User user4 = User.builder()
                .email("cat4@mail.ru")
                .login("Мур4")
                .name("Барсик")
                .birthday(LocalDate.of(2022, 5, 3))
                .build();
        userController.createUser(user4);
        filmController.addLike(3, 1);
        filmController.addLike(3, 2);
        filmController.addLike(3, 3);
        filmController.addLike(2, 1);
        assertEquals(5, filmController.displayPopularMovies(10).size());
        assertEquals(3, filmController.getFilm(3).getLikes().size());
        filmController.deleteLike(3, 1);
        assertEquals(2, filmController.getFilm(3).getLikes().size());
        List<Film> list = filmController.displayPopularMovies(2);
        for (Film film : list) {
            System.out.println(film);
        }

    }
}
