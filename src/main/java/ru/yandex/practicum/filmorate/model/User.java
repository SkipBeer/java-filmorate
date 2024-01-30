package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    private int id;
    @EqualsAndHashCode.Exclude
    @Email
    private String email;
    @EqualsAndHashCode.Exclude
    @NotBlank
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    @Past
    private LocalDate birthday;

    @EqualsAndHashCode.Exclude
    private final Set<Integer> friends = new HashSet<>();

    public Integer addFriend(Integer id) {
        friends.add(id);
        return id;
    }

    public Integer deleteFriend(Integer id) {
        friends.remove(id);
        return id;
    }
}