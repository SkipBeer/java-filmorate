package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Component
public class LikesDbStorage implements LikesStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDbStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getLikesCount(Integer id) {
        return jdbcTemplate.queryForObject("SELECT COUNT(user_id) FROM likes WHERE film_id = :id",
                new MapSqlParameterSource("id", id), Integer.class);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO likes(film_id, user_id) VALUES(:film_id, :user_id)",
                new MapSqlParameterSource()
                        .addValue("film_id", filmId).addValue("user_id", userId));
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = :user_id AND film_id = :film_id",
                new MapSqlParameterSource()
                        .addValue("film_id", filmId).addValue("user_id", userId));
    }
}
