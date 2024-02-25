package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FilmMapper  implements RowMapper<Film> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final FilmDbStorage.GenresComparator comparator;

    public FilmMapper(NamedParameterJdbcTemplate jdbcTemplate, FilmDbStorage.GenresComparator comparator) {
        this.jdbcTemplate = jdbcTemplate;
        this.comparator = comparator;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(makeMpa(rs.getInt("id")));
        film.setGenres(makeGenres(rs.getInt("id")));
        return film;
    }

    private Set<Genre> makeGenres(int id) {
        String sql = "SELECT gf.genre_id, genres.name " +
                "from genres_films AS gf " +
                "LEFT OUTER JOIN genres ON gf.genre_id = genres.id " +
                "where gf.film_id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return new HashSet<>(jdbcTemplate.query(sql, sqlParameterSource,
                (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name"))))
                .stream().sorted(comparator).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Mpa makeMpa(int id) {
        String sql = "SELECT mpa.name, mpa.id " +
                "FROM films AS f " +
                "LEFT OUTER JOIN mpa ON f.mpa = mpa.id " +
                "WHERE f.id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, parameterSource,
                (rs, rowNum) -> new Mpa(rs.getInt("mpa.id"), rs.getString("mpa.name")))
                .stream().findAny().orElse(null);
    }
}
