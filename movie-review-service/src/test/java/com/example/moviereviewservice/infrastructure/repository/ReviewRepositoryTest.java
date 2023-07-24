package com.example.moviereviewservice.infrastructure.repository;

import com.example.moviereviewservice.config.AbstractTestcontainers;
import com.example.moviereviewservice.domain.entity.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@DataMongoTest
class ReviewRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private ReviewRepository repository;

    @BeforeEach
    void setUp() {
        var reviews = List.of(
                new Review("1", "1", "good", 9.0),
                new Review("2", "2", "better", 8.0),
                new Review("4", "2", "bad", 3.0),
                new Review("3", "3", "best", 7.0)
        );

        repository.saveAll(reviews)
                .log()
                .blockLast();

    }


    @AfterEach
    void tearDown() {
        repository.deleteAll()
                .block();
    }

    @Test
    void shouldFindReviewsByMovieInfoId() {
        var movieInfoId = "2";
        Flux<Review> reviewsByMovieInfoId = repository.findReviewsByMovieInfoId(movieInfoId);

        StepVerifier.create(reviewsByMovieInfoId)
                .expectNextCount(2)
                .verifyComplete();
    }


}