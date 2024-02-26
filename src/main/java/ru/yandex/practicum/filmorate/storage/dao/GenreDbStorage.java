package ru.yandex.practicum.filmorate.storage.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre get(Integer id) {
        return jdbcTemplate.query("SELECT * FROM genres WHERE id = :id",
                new MapSqlParameterSource("id", id),
                new BeanPropertyRowMapper<>(Genre.class)).stream().findAny().orElse(null);
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres", new BeanPropertyRowMapper<>(Genre.class));
    }
}
