package ru.yandex.practicum.filmorate.model;

import lombok.*;

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

    @EqualsAndHashCode.Exclude
    private Set<Integer> likes;

    public Integer addLike(Integer id) {
        likes.add(id);
        return id;
    }

    public Integer deleteLike(Integer id) {
        likes.remove(id);
        return id;
    }

    public void setLikes() {
        this.likes = new HashSet<>();
    }
}