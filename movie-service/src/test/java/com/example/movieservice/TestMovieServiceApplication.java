package com.example.movieservice;

import com.example.movieservice.config.AbstractTestcontainers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers(AbstractTestcontainers.class)
public class TestMovieServiceApplication {
    public static void main(String[] args) {
        SpringApplication.from(MovieServiceApplication::main)
                .with(TestMovieServiceApplication.class)
                .run(args);
    }
}
