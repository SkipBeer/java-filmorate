package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.exceptions.InvalidTextFieldsException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
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
        Film film = filmService.getFilmById(id);
        if (film == null) {
            throw new UnknownFilmException("Фильм с id " + id + " не найден");
        }
        return film;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        log.debug("Получен запрос на добавление фильма, Переданная сущность: '{}'", film.toString());
        validation(film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        log.debug("Получен запрос на обновление фильма, Переданная сущность: '{}'", film.toString());
        validation(film);
        Film film1 = filmService.updateFilm(film);
        if (film1 == null) {
            throw new UnknownFilmException("Фильм с id " + film.getId() + " не существует");
        }
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        Film film = filmService.addLike(id, userId);
        if (film == null) {
            throw new UnknownFilmException("Фильм или пользователь с таким id не существует");
        }
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        Film film = filmService.deleteLike(id, userId);
        if (film == null) {
            throw new UnknownFilmException("Фильм или пользователь с таким id не существует");
        }
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getMostPopularFilms(Integer.parseInt(count));
    }

    public void validation(Film film) {

        if (film.getName() == null || film.getDescription().length() > 200 || film.getName().isBlank()) {
            throw new InvalidTextFieldsException("Название фильма не может быть пустым, описание не должно превышать" +
                    " 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)) || film.getDuration() < 0) {
            throw new InvalidDateException("Длительность фильмы не может быть отрицательной, дата выпуска" +
                    " должна быть после 28.12.1895");
        }
    }

}