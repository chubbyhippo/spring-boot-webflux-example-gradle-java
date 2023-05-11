package com.example.moviereviewservice.exception;

public class ReviewDtoException extends RuntimeException {
    public ReviewDtoException(String errorMessage) {
        super(errorMessage);
    }
}
