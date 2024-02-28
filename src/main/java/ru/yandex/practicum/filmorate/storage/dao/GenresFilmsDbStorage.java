package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresFilmsStorage;

import java.util.*;

@Component
public class GenresFilmsDbStorage implements GenresFilmsStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GenresFilmsDbStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Film film) {
        Set<Genre> genres = film.getGenres();
        Integer id = film.getId();
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO genres_films(film_id, genre_id) VALUES(:film_id, :genre_id)",
                new MapSqlParameterSource()
                        .addValue("film_id", id)
                        .addValue("genre_id", genre.getId()));
        }
    }

    @Override
    public void remove(Integer id) {
        jdbcTemplate.update("DELETE FROM genres_films WHERE film_id = :id",
                new MapSqlParameterSource("id", id));
    }

    @Override
    public Set<Genre> getGenresForFilm(Integer id) {
        return new HashSet<>(jdbcTemplate.query("SELECT g.name, g.id FROM genres_films AS gf " +
                        "LEFT JOIN genres AS g ON gf.genre_id = g.id WHERE gf.film_id = :id",
                new MapSqlParameterSource("id", id), new BeanPropertyRowMapper<>(Genre.class)));
    }

    @Override
    public Set<Genre> updateGenresForFilm(Film film) {
        remove(film.getId());
        add(film);
        return getGenresForFilm(film.getId());
    }
}
