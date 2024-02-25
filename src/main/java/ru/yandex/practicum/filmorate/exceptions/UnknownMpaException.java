package ru.yandex.practicum.filmorate.exceptions;

public class UnknownMpaException extends RuntimeException {
    public UnknownMpaException(String message) {
        super(message);
    }
}
