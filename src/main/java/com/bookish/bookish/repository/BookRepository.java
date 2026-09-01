package com.bookish.bookish.repository;

import com.bookish.bookish.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
