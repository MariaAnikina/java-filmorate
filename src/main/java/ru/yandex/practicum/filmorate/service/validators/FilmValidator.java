package ru.yandex.practicum.filmorate.service.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    public static void validate(Film film) {
        if (film.getName().isEmpty()) throw new ValidationException("Пустое имя фильма.");

        if (film.getDescription().length() > 200)
            throw new ValidationException("Превышен допустимый размер описания.");

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28")))
            throw new ValidationException(
                    "Дата релиза не может быть раньше даты релиза первого фильма в истории."
            );

        if (film.getDuration() <= 0) throw new ValidationException("Отрицательная продолжительность фильма.");
    }
}