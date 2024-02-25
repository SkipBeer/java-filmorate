package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenresFilmsStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;

import java.util.*;
import java.util.stream.Collectors;


@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final GenresFilmsStorage genresFilmsStorage;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final GenresComparator comparator = new GenresComparator();

    public FilmDbStorage(NamedParameterJdbcTemplate jdbcTemplate, GenresFilmsDbStorage genresFilmsStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresFilmsStorage = genresFilmsStorage;
    }

    public Film add(Film film) {
        String sqlQuery = "INSERT INTO films(name, release_date, description, duration, mpa)" +
                "VALUES(:name, :release_date, :description, :duration, :mpa)";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("release_date", film.getReleaseDate())
                .addValue("description", film.getDescription())
                .addValue("duration", film.getDuration())
                .addValue("mpa", film.getMpa().getId());
        jdbcTemplate.update(sqlQuery, sqlParameterSource, holder);
        film.setId(holder.getKey().intValue());
        if (film.getGenres() != null) {
            genresFilmsStorage.add(film);
        }
        return film;
    }

    public Film update(Film film) {
        String sql = "update films set name = :name, release_date = :release_date," +
                " description = :description, duration = :duration, mpa = :mpa where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", film.getId())
                .addValue("name", film.getName())
                .addValue("release_date", film.getReleaseDate())
                .addValue("description", film.getDescription())
                .addValue("duration", film.getDuration())
                .addValue("mpa", film.getMpa().getId());
        int status = jdbcTemplate.update(sql, sqlParameterSource);
        if (status == 0) {
            return null;
        }
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            genres = genresFilmsStorage.updateGenresForFilm(film);
            genres = genres.stream().sorted(comparator).collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        }
        return film;
    }

    public Film remove(Film film) {
        String sql = "delete from films where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", film.getId());
        jdbcTemplate.update(sql, sqlParameterSource);
        genresFilmsStorage.remove(film.getId());
        return film;
    }

    public Film getFilmById(Integer id) {
        String sql = "SELECT * from films where films.id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, sqlParameterSource, new FilmMapper(jdbcTemplate, comparator)).stream().findAny().orElse(null);
    }

    public List<Film> getFilms() {
        return jdbcTemplate.query("select * from films", new FilmMapper(jdbcTemplate, comparator));
    }

    public static class GenresComparator implements Comparator<Genre> {

        @Override
        public int compare(Genre o1, Genre o2) {
            return Integer.compare(o1.getId(), o2.getId());
        }
    }
}
