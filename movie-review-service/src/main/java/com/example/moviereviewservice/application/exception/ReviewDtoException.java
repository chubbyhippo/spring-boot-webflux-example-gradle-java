package com.example.moviereviewservice.application.exception;

public class ReviewDtoException extends RuntimeException {
    public ReviewDtoException(String errorMessage) {
        super(errorMessage);
    }
}
