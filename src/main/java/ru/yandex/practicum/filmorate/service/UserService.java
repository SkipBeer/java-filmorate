package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private int generatorId = 0;

    private int generateId() {
        return ++generatorId;
    }

    public User createUser(User user) {
        user.setFriends();
        user.setId(generateId());
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        user.setFriends();
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUserFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : userStorage.getUserById(id).getFriends()) {
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
        return userStorage.getUserById(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(userId) == null) {
            return null;
        }
        userStorage.getUserById(userId).deleteFriend(friendId);
        return userStorage.getUserById(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        List<User> result = new ArrayList<>();

        for (Integer friendId : userStorage.getUserById(userId).getFriends()) {
            if (userStorage.getUserById(otherId).getFriends().contains(friendId)) {
                result.add(userStorage.getUserById(friendId));
            }
        }
        return result;
    }
}
