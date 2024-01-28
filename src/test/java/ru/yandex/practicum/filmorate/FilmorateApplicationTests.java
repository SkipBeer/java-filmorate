
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.InvalidTextFieldsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

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
		film = new Film(0, "name", "desc",
				LocalDate.of(2000,10,20), 120, new HashSet<>());

		user = new User(0, "email@ya.ru", "login",
				"name", LocalDate.of(2003,11,24), new HashSet<>());
	}

	@Test
	void contextLoads() {
	}

	@Test
	void blankFilmNameTest() {
		film.setName(null);
		final InvalidTextFieldsException exception = assertThrows(
				InvalidTextFieldsException.class,
				new Executable() {
					@Override
					public void execute() {
						filmController.validation(film);
					}
				});
	}

	@Test
	void incorrectFilmDescTest() {
		film.setDescription("abc".repeat(200));

		final InvalidTextFieldsException exception = assertThrows(
				InvalidTextFieldsException.class,
				new Executable() {
					@Override
					public void execute() {
						filmController.validation(film);
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
						filmController.validation(film);
					}
				});
	}

	@Test
	void incorrectFilmDurationTest() {
		film.setDuration(-120);

		final InvalidDateException exception = assertThrows(
				InvalidDateException.class,
				new Executable() {
					@Override
					public void execute() {
						filmController.validation(film);
					}
				});
	}


	@Test
	void blankEmailTest() {
		user.setEmail(null);

		final InvalidEmailException exception = assertThrows(
				InvalidEmailException.class,
				new Executable() {
					@Override
					public void execute() {
						userController.validation(user);
					}
				});
	}

	@Test
	void incorrectEmailTest() {
		user.setEmail(" ");

		final InvalidEmailException exception = assertThrows(
				InvalidEmailException.class,
				new Executable() {
					@Override
					public void execute() {
						userController.validation(user);
					}
				});
	}

	@Test
	void blankLoginTest() {
		user.setLogin(null);

		final InvalidLoginException exception = assertThrows(
				InvalidLoginException.class,
				new Executable() {
					@Override
					public void execute() {
						userController.validation(user);
					}
				});
	}

	@Test
	void incorrectLoginTest() {
		user.setLogin("pro bel");

		final InvalidLoginException exception = assertThrows(
				InvalidLoginException.class,
				new Executable() {
					@Override
					public void execute() {
						userController.validation(user);
					}
				});
	}

	@Test
	void incorrectBirthdayTest() {
		user.setBirthday(LocalDate.of(3000,10,10));

		final InvalidDateException exception = assertThrows(
				InvalidDateException.class,
				new Executable() {
					@Override
					public void execute() {
						userController.validation(user);
					}
				});
	}

}

