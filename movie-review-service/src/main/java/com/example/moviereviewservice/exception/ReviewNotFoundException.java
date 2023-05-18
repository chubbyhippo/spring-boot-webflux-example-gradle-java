package com.example.moviereviewservice.exception;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
