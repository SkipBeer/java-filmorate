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
    public Film create(@RequestBody Film film) throws InvalidDateException, InvalidTextFieldsException {

        log.debug("Получен запрос на добавление фильма, Переданная сущность: '{}'", film.toString());

        if (!validation(film)) return null;

        film.setId(generateId());

        films.add(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws UnknownFilmException, InvalidDateException, InvalidTextFieldsException {

        log.debug("Получен запрос на обновление фильма, Переданная сущность: '{}'", film.toString());

        if (!validation(film)) {
            return null;
        }


        boolean findFlag = false;
        for (Film oldFilm : films) {
            if (film.getId() == oldFilm.getId()) {
                findFlag = true;
                films.remove(oldFilm);
                films.add(film);
            }
        }

        if (!findFlag) throw new UnknownFilmException();

        return film;
    }

    public boolean validation(Film film) throws InvalidTextFieldsException, InvalidDateException {

        if (film.getName() == null || film.getDescription().length() > 200) {
            throw new InvalidTextFieldsException();
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)) || film.getDuration() < 0) {
            throw new InvalidDateException();
        }

        return true;
    }

}
