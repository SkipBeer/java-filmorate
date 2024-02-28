package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final LikesComparator likesComparator = new LikesComparator();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new InvalidDateException("дата выпуска фильма должна быть после 28.12.1895");
        }
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new InvalidDateException("дата выпуска фильма должна быть после 28.12.1895");
        }
        Film updatedFilm = filmStorage.update(film);
        if (updatedFilm == null) {
            throw new UnknownFilmException("Фильм с id " + film.getId() + " не существует");
        }
        return updatedFilm;
    }

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getFilms());
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new UnknownFilmException("Фильм с id " + id + " не найден");
        }
        return film;
    }

    public Film deleteFilm(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new UnknownFilmException("Фильм с id " + id + " не найден");
        }
        return filmStorage.remove(film);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
            throw new UnknownFilmException("Фильм или пользователь с таким id не существует");
        }
        likesStorage.addLike(filmId, userId);
        return filmStorage.update(filmStorage.getFilmById(filmId));
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
            throw new UnknownFilmException("Фильм или пользователь с таким id не существует");
        }
        likesStorage.deleteLike(filmId, userId);
        return filmStorage.update(filmStorage.getFilmById(filmId));
    }

    public List<Film> getMostPopularFilms(int count) {
        List<Film> filmsList = new ArrayList<>(filmStorage.getFilms());
        for (Film film : filmsList) {
            film.setLikes(likesStorage.getLikesCount(film.getId()));
        }
        filmsList.sort(likesComparator);
        if (count > filmsList.size()) {
            return filmsList;
        }
        return filmsList.subList(0, count);
    }

    private static class LikesComparator implements Comparator<Film> {

        @Override
        public int compare(Film o1, Film o2) {
            return Integer.compare(o2.getLikes(), o1.getLikes());
        }
    }

}
