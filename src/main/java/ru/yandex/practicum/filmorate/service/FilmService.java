package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getFilms());
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public Film deleteFilm(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            return null;
        }
        return filmStorage.remove(film);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
            return null;
        }
        likesStorage.addLike(filmId, userId);
        return filmStorage.update(filmStorage.getFilmById(filmId));
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
            return null;
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
