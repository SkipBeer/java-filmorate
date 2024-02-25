package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.util.List;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User add(User user) {
        String sqlQuery = "insert into users(email, login, name, birthday)" +
                "values (:email, :login, :name, :birthday)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());
        KeyHolder holder = new GeneratedKeyHolder();
        int id = jdbcTemplate.update(sqlQuery, parameterSource, holder);
        if (id == 0) {
            return null;
        }
        user.setId(holder.getKey().intValue());
        return user;
    }

    public User update(User user) {
        String sqlQuery = "update users set " +
                "email = :email, login = :login, name = :name, birthday = :birthday " +
                "where id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday())
                .addValue("id", user.getId());
        int id = jdbcTemplate.update(sqlQuery, parameterSource);
        if (id == 0) {
            return null;
        }
        return user;
    }

    public User getUserById(Integer id) {
        return jdbcTemplate.query("select * from users where id = :id",
                        new MapSqlParameterSource("id", id),
                        new UserMapper()).stream().findAny().orElse(null);
    }

    public List<User> getUsers() {
        return jdbcTemplate.query("select * from users", new UserMapper());
    }

    public User remove(User user) {
        jdbcTemplate.update("delete from users where id = :id",
                new MapSqlParameterSource("id", user.getId()));
        return user;
    }
}
