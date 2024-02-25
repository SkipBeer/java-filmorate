package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {

    @EqualsAndHashCode.Include
    private int id;
    @NotBlank(message = "название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "описание фильма не должно превышать 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "длина фильма не может быть отрицательной")
    private int duration;

    private Set<Genre> genres;

    private Mpa mpa;

    private Integer likes;
}