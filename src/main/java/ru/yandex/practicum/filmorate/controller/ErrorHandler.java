package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({InvalidDateException.class, InvalidEmailException.class, InvalidLoginException.class,
            InvalidTextFieldsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDateException(final RuntimeException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler({UnknownFilmException.class, UnknownUserException.class,
            UnknownMpaException.class, UnknownGenreException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUnknownFilmException(final RuntimeException e) {
        return new ErrorResponse(
                e.getMessage()
        );
    }
}
