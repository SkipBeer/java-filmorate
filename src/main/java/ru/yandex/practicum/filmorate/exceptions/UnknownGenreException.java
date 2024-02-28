package ru.yandex.practicum.filmorate.exceptions;

public class UnknownGenreException extends RuntimeException {
    public UnknownGenreException(String message) {
        super(message);
    }
}
