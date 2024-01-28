package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidateException extends RuntimeException {
    public FilmValidateException(String message) {
        super(message);
    }
}
