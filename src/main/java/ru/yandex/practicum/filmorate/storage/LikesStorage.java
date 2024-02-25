package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Integer getLikesCount(Integer id);
}
