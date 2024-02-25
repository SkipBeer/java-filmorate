CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar,
    description text,
    release_date timestamp,
    duration integer,
    mpa integer
);

CREATE TABLE IF NOT EXISTS likes (
    user_id integer,
    film_id integer
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar,
    login varchar,
    name varchar,
    birthday timestamp
);

CREATE TABLE IF NOT EXISTS friends (
    user_id integer,
    friend_id integer,
    status text
);

CREATE TABLE IF NOT EXISTS genres (
    id integer,
    name varchar
);

CREATE TABLE IF NOT EXISTS genres_films (
    film_id integer,
    genre_id integer
);

CREATE TABLE IF NOT EXISTS mpa (
    id integer,
    name varchar
);

