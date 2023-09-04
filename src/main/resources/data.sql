MERGE INTO RATING (NAME) KEY (NAME)
    VALUES ('G'),
           ('PG'),
           ('PG-13'),
           ('R'),
           ('NC-17');


MERGE INTO GENRE (GENRE_NAME) KEY (GENRE_NAME )
    VALUES ('Комедия'),
           ('Драма'),
           ('Мультфильм'),
           ('Триллер'),
           ('Документальный'),
           ('Боевик');