package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
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

    private final HashMap<Integer, FriendshipStatus> friends = new HashMap<>();

    public Integer addFriend(Integer id) {
        friends.put(id, FriendshipStatus.unconfirmed);
        return id;
    }

    public Integer deleteFriend(Integer id) {
        friends.remove(id);
        return id;
    }

    public void confirmFriendship(Integer id) {
        friends.put(id, FriendshipStatus.confirmed);
    }
}