


create table if not exists GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(63),
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);
create table if not exists RATING
(
    ID INTEGER auto_increment,
    NAME      CHARACTER VARYING(20) not null,
    constraint "RATING_pk"
        primary key (ID)
);
create table if not exists USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(100) not null,
    LOGIN    CHARACTER VARYING(100) not null,
    NAME     CHARACTER VARYING(100),
    BIRTHDAY DATE,
    constraint "USER_pk"
        primary key (USER_ID)
);

create table if not exists FILM
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(50) not null,
    DESCRIPTION  CHARACTER VARYING(511),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATE         INTEGER,
    MPA_ID    INTEGER,
    constraint "FILM_pk"
        primary key (FILM_ID),
    constraint "FILM_RATING_RATING_ID_fk"
        foreign key (MPA_ID) references RATING
);

create table if not exists FILM_GENRE
(
    FILM_ID       INTEGER,
    GENRE_ID      INTEGER,
    constraint "FILM_GENRE_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "FILM_GENRE_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE
);
create table if not exists MOVIE_LIKE
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint "MOVIE_LIKE_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "MOVIE_LIKE_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS
);



create table if not exists FRIEND
(
    USER_ID        INTEGER,
    FRIEND_ID      INTEGER,
    constraint "FRIEND_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS,
    constraint "FRIEND_USERS_USER_ID_fk2"
        foreign key (FRIEND_ID) references USERS
);

