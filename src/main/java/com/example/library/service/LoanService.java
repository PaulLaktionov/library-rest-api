package com.example.library.service;

import com.example.library.exception.NotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.Reader;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final LoanRepository loanRepository;

    public Loan issueBook (Long bookId, Long readerId){

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(Book.class, bookId));

        if (book.getAvailableCopies() <= 0){
            throw new IllegalArgumentException("No available copies for book with id " + bookId);
        }
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new NotFoundException(Reader.class, readerId));
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);


        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setReaderId(reader.getId());
        loan.setLoanDate(LocalDateTime.now());
        return loanRepository.save(loan);
    }
    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new NotFoundException(Loan.class, loanId));
        if (loan.getReturnDate() != null) {
            throw new IllegalArgumentException("Loan with id " + loanId + " is already returned");
        }
        Book book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new NotFoundException(Book.class, loan.getBookId()));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        loan.setReturnDate(LocalDateTime.now());


        return loanRepository.save(loan);
    }
    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    }



