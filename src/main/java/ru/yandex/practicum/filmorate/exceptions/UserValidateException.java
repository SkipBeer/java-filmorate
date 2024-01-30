package ru.yandex.practicum.filmorate.exceptions;

public class UserValidateException extends RuntimeException {
    public UserValidateException(String message) {
        super(message);
    }
}
