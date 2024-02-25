package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenresFilmsStorage {

    void add(Film film);

    void remove(Integer id);

    Set<Genre> getGenresForFilm(Integer id);

    Set<Genre> updateGenresForFilm(Film film);
}
