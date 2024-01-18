package ru.yandex.practicum.filmorate;

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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	FilmController filmController = new FilmController();
	UserController userController = new UserController();

	@Test
	void contextLoads() {
	}

	@Test
	void blankFilmNameTest() {
		Film film = new Film(0, null, "desc",
				LocalDate.of(2000,10,20), 120);

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
		Film film = new Film(0, "name", "desc".repeat(200),
				LocalDate.of(2000,10,20), 120);

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
		Film film = new Film(0, "name", "desc",
				LocalDate.of(1800,10,20), 120);

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
		Film film = new Film(0, "name", "desc",
				LocalDate.of(1800,10,20), -120);

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
		User user = new User(0, null, "login",
				"name", LocalDate.of(2003,11,24));

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
		User user = new User(0, " ", "login",
				"name", LocalDate.of(2003,11,24));

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
		User user = new User(0, "pochta@ya.ru", null,
				"name", LocalDate.of(2003,11,24));

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
		User user = new User(0, "pochta@ya.ru", "pro bel",
				"name", LocalDate.of(2003,11,24));

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
		User user = new User(0, "pochta@ya.ru", "login",
				"name", LocalDate.of(3000,10,10));

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
