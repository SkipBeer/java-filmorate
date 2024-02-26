package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            return null;
        }
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public User remove(User user) {
        users.remove(user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            return null;
        }
        return users.get(userId);
    }
}
