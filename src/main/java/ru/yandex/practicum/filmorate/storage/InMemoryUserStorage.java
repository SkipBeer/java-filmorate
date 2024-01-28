package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {

    HashMap<Integer, User> users = new HashMap<>();

    public User add(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            return null;
        }
        users.replace(user.getId(), user);
        return user;
    }

    public User remove(User user) {
        users.remove(user.getId());
        return user;
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public User getUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            return null;
        }
        return users.get(userId);
    }
}
