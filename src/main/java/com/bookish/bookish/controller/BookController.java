package com.bookish.bookish.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/books")
    public List<Map<String, Object>> books() {
        return List.of(Map.of("id", 1, "title", "Sample Book", "author", "Author Name"));
    }
}
