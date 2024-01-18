package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Film {

    @EqualsAndHashCode.Include
    private int id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String description;

    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;

    @EqualsAndHashCode.Exclude
    private int duration;
}