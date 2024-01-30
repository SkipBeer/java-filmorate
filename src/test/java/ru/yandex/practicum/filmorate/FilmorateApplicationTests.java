
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	FilmStorage filmStorage = new InMemoryFilmStorage();
	UserStorage userStorage = new InMemoryUserStorage();
	FilmService filmService = new FilmService(filmStorage, userStorage);
	UserService userService = new UserService(userStorage);
	FilmController filmController = new FilmController(filmService);
	UserController userController = new UserController(userService);

	Film film;
	User user;

	@BeforeEach
	void createData() {
		film = new Film();

		user = new User();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void blankFilmNameTest() {
		film.setName(null);
		final NullPointerException exception = assertThrows(
				NullPointerException.class,
				new Executable() {
					@Override
					public void execute() {
						filmController.create(film);
					}
				});
	}

	@Test
	void incorrectFilmDescTest() {
		film.setDescription("abc".repeat(200));

		final NullPointerException exception = assertThrows(
				NullPointerException.class,
				new Executable() {
					@Override
					public void execute() {
						filmController.create(film);
					}
				});
	}

	@Test
	void incorrectFilmReleaseDateTest() {
		film.setReleaseDate(LocalDate.of(1800,10,9));

		final InvalidDateException exception = assertThrows(
				InvalidDateException.class,
				new Executable() {
					@Override
					public void execute() {
						filmController.create(film);
					}
				});
	}

	@Test
	void incorrectFilmDurationTest() {
		film.setDuration(-120);

		final NullPointerException exception = assertThrows(
				NullPointerException.class,
				new Executable() {
					@Override
					public void execute() {
						filmController.create(film);
					}
				});
	}
}

