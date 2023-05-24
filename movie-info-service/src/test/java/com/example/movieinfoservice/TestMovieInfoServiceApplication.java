package com.example.movieinfoservice;

import com.example.movieinfoservice.config.MovieInfoServiceContainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestMovieInfoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.from(MovieInfoServiceApplication::main)
                .with(MovieInfoServiceContainersConfiguration.class)
                .run(args);
    }
}