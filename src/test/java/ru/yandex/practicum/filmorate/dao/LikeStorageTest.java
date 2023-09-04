package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeStorageTest {
    private final LikeStorage storage;

    @Test
    void shouldAddDeleteGetLikes(@Qualifier("databaseUser") UserStorage userStorage,
                                 @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        Film film = new Film("Оно", "Красный шарик",
                LocalDate.parse("2005-10-20"), 90, new RatingMPA(1, "G"));
        filmStorage.create(film);
        User user = new User(1, "cat@email.com", "cat",
                "cat", LocalDate.parse("2000-05-25"), new HashSet<>());
        userStorage.create(user);
        storage.addLike(film.getId(), user.getId());
        List<Integer> likes = storage.getLikes(film.getId());

        assertEquals(List.of(user.getId()), likes);

        storage.deleteLike(film.getId(), user.getId());
        likes = storage.getLikes(film.getId());

        assertTrue(likes.isEmpty());
    }
}