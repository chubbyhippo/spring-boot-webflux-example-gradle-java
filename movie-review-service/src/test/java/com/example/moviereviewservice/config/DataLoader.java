package com.example.moviereviewservice.config;

import com.example.moviereviewservice.entity.Review;
import com.example.moviereviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final ReviewRepository repository;

    @Bean
    ApplicationRunner loader() {
        return args -> {
            var reviews = List.of(
                    new Review("1", "1", "good", 9.0),
                    new Review("2", "2", "better", 8.0),
                    new Review("3", "3", "best", 7.0),
                    new Review("4", "3", "da best", 8.0)
            );

            repository.saveAll(reviews)
                    .log()
                    .blockLast();
        };
    }


}
