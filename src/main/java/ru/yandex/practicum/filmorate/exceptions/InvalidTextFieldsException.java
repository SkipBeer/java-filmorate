package ru.yandex.practicum.filmorate.exceptions;

public class InvalidTextFieldsException extends RuntimeException {
    public InvalidTextFieldsException(String message) {
        super(message);
    }
}
