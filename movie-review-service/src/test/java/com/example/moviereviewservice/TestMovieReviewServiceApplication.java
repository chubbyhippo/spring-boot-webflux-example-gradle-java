package com.example.moviereviewservice;

import com.example.moviereviewservice.config.MovieReviewServiceContainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestMovieReviewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.from(MovieReviewServiceApplication::main)
                .with(MovieReviewServiceContainersConfiguration.class)
                .run(args);
    }
}
