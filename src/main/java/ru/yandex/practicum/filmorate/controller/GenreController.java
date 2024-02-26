package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private static final Logger log = LoggerFactory.getLogger(GenreController.class);

    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<Genre> findAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable("id") Integer id) {
        return service.get(id);
    }
}
