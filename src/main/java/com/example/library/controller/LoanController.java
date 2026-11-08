package com.example.library.controller;

import com.example.library.model.Loan;
import com.example.library.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public List<Loan> getAll() {
        return loanService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan issue(@Valid @RequestBody Loan loan) {
        return loanService.issueBook(loan.getBookId(), loan.getReaderId());
    }
    @PostMapping("/{id}/return")
    public Loan returnBook(@PathVariable Long id) {
        return loanService.returnBook(id);
    }
}