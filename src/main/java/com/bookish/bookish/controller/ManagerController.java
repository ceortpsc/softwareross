package com.bookish.bookish.controller;

import com.bookish.bookish.repository.BookRepository;
import com.bookish.bookish.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public ManagerController(BookRepository bookRepo, UserRepository userRepo) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/stats")
    public Map<String,Object> stats(){
        return Map.of(
            "books", bookRepo.count(),
            "users", userRepo.count()
        );
    }
}
