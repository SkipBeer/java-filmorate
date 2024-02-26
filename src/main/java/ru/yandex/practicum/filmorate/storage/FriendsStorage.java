package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendsStorage {

    void addFriend(Integer userid, Integer friendId);

    Integer confirmFriendship(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<Integer> getFriends(Integer userId);
}
