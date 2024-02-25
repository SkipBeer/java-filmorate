package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendsStorage {

    Integer addFriend(Integer userid, Integer friendId);

    Integer confirmFriendship(Integer userId, Integer friendId);

    Integer deleteFriend(Integer userId, Integer friendId);

    List<Integer> getFriends(Integer userId);
}
