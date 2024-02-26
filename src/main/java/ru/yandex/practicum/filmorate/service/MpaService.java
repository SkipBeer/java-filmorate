package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UnknownMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage storage;

    @Autowired
    public MpaService(MpaDbStorage storage) {
        this.storage = storage;
    }

    public Mpa get(Integer id)  {
        Mpa mpa = storage.get(id);
        if (mpa == null) {
            throw new UnknownMpaException("MPA с id " + id + " не существует");
        }
        return mpa;
    }

    public List<Mpa> getAll() {
        return storage.getAll();
    }
}
