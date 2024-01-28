package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Film remove(Film film);

    HashMap<Integer, Film> getFilms();

    Film getFilmById(Integer id);
}
