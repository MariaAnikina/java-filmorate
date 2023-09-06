package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> likes;
    private Integer rate;
    private RatingMPA mpa;
    private List<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration,
                Set<Integer> likes, RatingMPA mpa, List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, RatingMPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, RatingMPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Integer rate,
                RatingMPA mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Integer rate,
                RatingMPA mpa, List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration,
                Set<Integer> likes, Integer rate, RatingMPA mpa, List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public static int compare(Film f1, Film f2) {
        if (f2.getRate() != null) {
            if (f1.getRate() != null) {
                if (f2.getRate() != f1.getRate()) {
                    return f2.getRate() - f1.getRate();
                }
            } else {
                return 1;
            }
        } else if (f1.getRate() != null) {
            return -1;
        }
        return f2.getId() - f1.getId();
    }
}