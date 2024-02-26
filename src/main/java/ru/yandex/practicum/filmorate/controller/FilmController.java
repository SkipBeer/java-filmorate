package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        List<Film> films = filmService.getAll();
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") Integer id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос на добавление фильма, Переданная сущность: '{}'", film.toString());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос на обновление фильма, Переданная сущность: '{}'", film.toString());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable Integer id) {
        return filmService.deleteFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getMostPopularFilms(Integer.parseInt(count));
    }
}