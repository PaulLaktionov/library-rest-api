package com.example.library.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> entityClass, Long id) {
        super(entityClass.getSimpleName() + " with id " + id + " not found");

    }
}