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
import ru.yandex.practicum.filmorate.exceptions.UpdateFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
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

    @Autowired
    public FilmDbStorage(NamedParameterJdbcTemplate jdbcTemplate, GenresFilmsDbStorage genresFilmsStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresFilmsStorage = genresFilmsStorage;
    }

    @Override
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

    @Override
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
            throw new UpdateFilmException("Ошибка при обновлении фильма с id " + film.getId());
        }
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            genres = genresFilmsStorage.updateGenresForFilm(film);
            genres = genres.stream().sorted(comparator).collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        }
        return film;
    }

    @Override
    public Film remove(Film film) {
        String sql = "delete from films where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", film.getId());
        jdbcTemplate.update(sql, sqlParameterSource);
        genresFilmsStorage.remove(film.getId());
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sql = "SELECT * from films where films.id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        Film film = jdbcTemplate.query(sql, sqlParameterSource,
                new FilmMapper()).stream().findAny().orElse(null);
        if (film == null) {
            throw new UpdateFilmException("Фильм с id " + id + " не найден");
        }
        film.setMpa(makeMpa(film.getId()));
        film.setGenres(makeGenres(film.getId()));
        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query("select * from films", new FilmMapper());
        for (Film film : films) {
            film.setMpa(makeMpa(film.getId()));
            film.setGenres(makeGenres(film.getId()));
        }
        return films;
    }

    public static class GenresComparator implements Comparator<Genre> {

        @Override
        public int compare(Genre o1, Genre o2) {
            return Integer.compare(o1.getId(), o2.getId());
        }
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
