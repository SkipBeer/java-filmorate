package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Film {

    @EqualsAndHashCode.Include
    private int id;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "название фильма не может быть пустым")
    private String name;

    @EqualsAndHashCode.Exclude
    @Size(max = 200, message = "описание фильма не должно превышать 200 символов")
    private String description;

    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;

    @EqualsAndHashCode.Exclude
    @Positive(message = "длина фильма не может быть отрицательной")
    private int duration;

    @EqualsAndHashCode.Exclude
    private final Set<Integer> likes = new HashSet<>();

    public Integer addLike(Integer id) {
        likes.add(id);
        return id;
    }

    public Integer deleteLike(Integer id) {
        likes.remove(id);
        return id;
    }
}