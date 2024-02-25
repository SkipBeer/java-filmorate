package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa get(Integer id) {
      return jdbcTemplate.query("SELECT * FROM mpa WHERE id = :id",
              new MapSqlParameterSource("id", id),
              new BeanPropertyRowMapper<>(Mpa.class)).stream().findAny().orElse(null);
    }

    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM mpa", new BeanPropertyRowMapper<>(Mpa.class));
    }
}
