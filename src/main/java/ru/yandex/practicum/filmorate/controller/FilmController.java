package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.exceptions.InvalidTextFieldsException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final List<Film> films = new ArrayList<>();

    private int generatorId = 0;

    private int generateId() {
        return ++generatorId;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {

        log.debug("Получен запрос на добавление фильма, Переданная сущность: '{}'", film.toString());

        validation(film);

        film.setId(generateId());

        films.add(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {

        log.debug("Получен запрос на обновление фильма, Переданная сущность: '{}'", film.toString());

        validation(film);
        if (films.contains(film)) {
            films.remove(film);
            films.add(film);
        } else {
            throw new UnknownFilmException();
        }

        return film;
    }

    public void validation(Film film) {

        if (film.getName() == null || film.getDescription().length() > 200 || film.getName().isBlank()) {
            throw new InvalidTextFieldsException();
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)) || film.getDuration() < 0) {
            throw new InvalidDateException();
        }
    }

}