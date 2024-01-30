package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    HashMap<Integer, Film> films = new HashMap<>();

    public Film add(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            return null;
        }
        films.replace(film.getId(), film);
        return film;
    }

    public Film remove(Film film) {
        films.remove(film.getId());
        return film;
    }

    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    public Film getFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            return null;
        }
        return films.get(filmId);
    }
}
