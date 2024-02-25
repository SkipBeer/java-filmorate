package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public User createUser(User user) {
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public List<User> getAll() {
        List<User> users = userStorage.getUsers();
        for (User user : users) {
            user.setFriends(friendsStorage.getFriends(user.getId()));
        }
        return users;
    }

    public User getUserById(Integer id) {
        User user = userStorage.getUserById(id);
        if (user != null) {
            user.setFriends(friendsStorage.getFriends(id));
        }
        return user;
    }

    public User deleteUser(Integer id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            return null;
        }
        return userStorage.remove(user);
    }

    public List<User> getUserFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendsStorage.getFriends(id)) {
            friends.add(userStorage.getUserById(friendId));
        }
        return friends;
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            return null;
        }
        userStorage.getUserById(userId).addFriend(friendId);
        userStorage.getUserById(friendId).addFriend(userId);
        friendsStorage.addFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(userId) == null) {
            return null;
        }
        userStorage.getUserById(userId).deleteFriend(friendId);
        friendsStorage.deleteFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        List<User> result = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);

        List<Integer> friendsOfOtherUser = friendsStorage.getFriends(otherId);
        for (Integer friendId : friendsStorage.getFriends(userId)) {
            if (friendsOfOtherUser.contains(friendId)) {
                result.add(userStorage.getUserById(friendId));
            }
        }
        return result;
    }
}
