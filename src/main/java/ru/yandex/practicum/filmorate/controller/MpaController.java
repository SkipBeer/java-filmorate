package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.UnknownMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final MpaService service;

    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Mpa> findAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable("id") Integer id) {
        Mpa mpa = service.get(id);
        if (mpa == null) {
            throw new UnknownMpaException("MPA с id " + id + " не существует");
        }
        return service.get(id);
    }
}
