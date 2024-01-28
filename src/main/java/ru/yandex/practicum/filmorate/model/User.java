package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Include
    private int id;
    @EqualsAndHashCode.Exclude
    private String email;
    @EqualsAndHashCode.Exclude
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;

    @EqualsAndHashCode.Exclude
    private Set<Integer> friends;

    public Integer addFriend(Integer id) {
        friends.add(id);
        return id;
    }

    public Integer deleteFriend(Integer id) {
        friends.remove(id);
        return id;
    }

    public void setFriends() {
        this.friends = new HashSet<>();
    }

}