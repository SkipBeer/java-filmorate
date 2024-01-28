package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesComparator likesComparator = new LikesComparator();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private int generatorId = 0;

    private int generateId() {
        return ++generatorId;
    }

    public Film addFilm(Film film) {
        film.setLikes();
        film.setId(generateId());
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        film.setLikes();
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
            return null;
        }
        filmStorage.getFilmById(filmId).addLike(userId);
        return filmStorage.update(filmStorage.getFilmById(filmId));
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId) == null || userStorage.getUserById(userId) == null) {
            return null;
        }
        filmStorage.getFilmById(filmId).deleteLike(userId);
        return filmStorage.update(filmStorage.getFilmById(filmId));
    }

    public List<Film> getMostPopularFilms(int count) {
        List<Film> filmsList = new ArrayList<>(filmStorage.getFilms().values());
        filmsList.sort(likesComparator);
        if (count > filmsList.size()) {
            return filmsList;
        }
        return filmsList.subList(0, count);
    }

    static class LikesComparator implements Comparator<Film> {

        @Override
        public int compare(Film o1, Film o2) {
            return Integer.compare(o2.getLikes().size(), o1.getLikes().size());
        }
    }

}
