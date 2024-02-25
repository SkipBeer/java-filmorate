package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "insert into friends(user_id, friend_id, status)" +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, FriendshipStatus.unconfirmed.toString());
        return userId;
    }

    public Integer confirmFriendship(Integer userId, Integer friendId) {
        String sqlQuery = "update friends set status = ? where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, FriendshipStatus.confirmed.toString(), friendId, userId);
        return userId;
    }

    public Integer deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return userId;
    }

    public List<Integer> getFriends(Integer userId) {
        return jdbcTemplate.query("select friend_id from friends where user_id = ?", (rs, rowNum) -> getInt(rs), userId);
    }

    private Integer getInt(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }

}
