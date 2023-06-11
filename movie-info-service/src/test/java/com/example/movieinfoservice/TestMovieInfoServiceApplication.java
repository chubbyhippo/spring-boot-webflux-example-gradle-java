package com.example.movieinfoservice;

import com.example.movieinfoservice.config.AbstractTestcontainers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers(AbstractTestcontainers.class)
public class TestMovieInfoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.from(MovieInfoServiceApplication::main)
                .with(TestMovieInfoServiceApplication.class)
                .run(args);
    }
}