package com.bookish.bookish.controller;

import com.bookish.bookish.model.Book;
import com.bookish.bookish.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/editor")
public class EditorController {

    private final BookRepository bookRepo;

    public EditorController(BookRepository bookRepo) { this.bookRepo = bookRepo; }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book b) {
        Book saved = bookRepo.save(b);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book b) {
        Optional<Book> ob = bookRepo.findById(id);
        if (ob.isEmpty()) return ResponseEntity.notFound().build();
        Book exist = ob.get();
        exist.setTitle(b.getTitle());
        exist.setAuthor(b.getAuthor());
        bookRepo.save(exist);
        return ResponseEntity.ok(exist);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (!bookRepo.existsById(id)) return ResponseEntity.notFound().build();
        bookRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message","deleted"));
    }

    @GetMapping("/books")
    public List<Book> listBooks(){ return bookRepo.findAll(); }
}
