package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        List<User> users = userService.getAll();
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) {

        log.debug("Получен запрос на добавление фильма, Переданная сущность: '{}'", user.toString());
        validation(user);
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {

        log.debug("Получен запрос на обновление фильма, Переданная сущность: '{}'", user.toString());
        validation(user);
        User user1  = userService.updateUser(user);
        if (user1 == null) {
            throw new UnknownUserException("Пользователь с id " + user.getId() + " не существует");
        }
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new UnknownUserException("Пользователь с id " + id + " не существует");
        }
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
            User user = userService.addFriend(id, friendId);
            if (user == null) {
                throw new UnknownUserException("Пользователя с таким id не существует");
            }
            return user;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    public void validation(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
           user.setName(user.getLogin());
        }

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new InvalidEmailException("e-mail не может быть пустым и должен содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Логин не может быть пустым или содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidDateException("Ошибка даты рождения");
        }
    }
}