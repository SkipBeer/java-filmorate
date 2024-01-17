package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidDateException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final List<User> users = new ArrayList<>();

    private int generatorId = 0;

    private int generateId() {
        return ++generatorId;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws InvalidDateException, InvalidLoginException, InvalidEmailException {

        log.debug("Получен запрос на добавление фильма, Переданная сущность: '{}'", user.toString());

        if (!validation(user)) {
            return null;
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(generateId());

        users.add(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) throws InvalidEmailException, InvalidLoginException, InvalidDateException, UnknownUserException {

        log.debug("Получен запрос на обновление фильма, Переданная сущность: '{}'", user.toString());

        if (!validation(user)) {
            return null;
        }


        boolean findFlag = false;
        for (User oldUser : users) {
            if (user.getId() == oldUser.getId()) {
                findFlag = true;
                users.remove(oldUser);
                users.add(user);
            }
        }

        if (!findFlag) {
            throw new UnknownUserException();
        }

        return user;
    }

    public boolean validation(User user) throws InvalidEmailException, InvalidLoginException, InvalidDateException {

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new InvalidEmailException();
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new InvalidLoginException();
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidDateException();
        }

        return true;
    }
}