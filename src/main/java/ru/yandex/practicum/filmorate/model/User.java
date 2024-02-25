package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {

    @EqualsAndHashCode.Include
    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    private final List<Integer> friends = new ArrayList<>();

    public Integer addFriend(Integer id) {
        friends.add(id);
        return id;
    }

    public Integer deleteFriend(Integer id) {
        friends.remove(id);
        return id;
    }

    public void setFriends(List<Integer> newFriends) {
        friends.addAll(newFriends);
    }
}