package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UnknownGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage storage;

    @Autowired
    public GenreService(GenreDbStorage storage) {
        this.storage = storage;
    }

    public Genre get(Integer id) {
        Genre genre = storage.get(id);
        if (genre == null) {
            throw new UnknownGenreException("Жанра с id " + id + " не существует");
        }
        return genre;
    }

    public List<Genre> getAll() {
        return storage.getAll();
    }
}
