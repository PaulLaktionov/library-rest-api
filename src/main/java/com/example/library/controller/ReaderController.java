package com.example.library.controller;

import com.example.library.model.Reader;
import com.example.library.service.ReaderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;

    @GetMapping
    public List<Reader> getAll() {
        return readerService.getAll();
    }
    @GetMapping("/{id}")
    public Reader getById(@PathVariable Long id){
        return readerService.getById(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reader  create(@Valid @RequestBody Reader reader){
        return readerService.create(reader);

    }
    @PutMapping("/{id}")
    public Reader update(@PathVariable Long id,@Valid @RequestBody Reader updated) {
        return readerService.update(id, updated);

    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        readerService.delete(id);
    }
}
