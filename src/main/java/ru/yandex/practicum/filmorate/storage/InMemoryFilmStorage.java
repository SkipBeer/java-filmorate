package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            return null;
        }
        films.replace(film.getId(), film);
        return film;
    }

    @Override
    public Film remove(Film film) {
        films.remove(film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            return null;
        }
        return films.get(filmId);
    }
}
