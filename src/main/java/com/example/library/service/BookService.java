package com.example.library.service;
import com.example.library.exception.NotFoundException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAll(){
        return bookRepository.findAll();
    }
    public Book  getById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Book.class, id));
    }
    public Book  create(Book book){
        return bookRepository.save(book);
    }
    public Book update(Long id, Book updated) {
        Book book = getById(id);
        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setAvailableCopies(updated.getAvailableCopies());
        return bookRepository.save(book);

    }
    public void delete(Long id) {
        Book book = getById(id);
        bookRepository.delete(book);
    }

}
