package com.example.movieinfoservice.repository;

import com.example.movieinfoservice.document.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

@DataMongoTest
@Testcontainers
class MovieInfoRepositoryTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getConnectionString);
        registry.add("spring.data.mongodb.database", () -> "database_name");
    }

    @Autowired
    private MovieInfoRepository repository;

    @BeforeEach
    void setup() {
        var movieInfos = List.of(
                new MovieInfo(null,
                        "Nobody",
                        2021,
                        List.of("Bob Odenkirk", "Connie Nielsen"),
                        LocalDate.of(2021, 4, 13)),
                new MovieInfo(null,
                        "John Wick: Chapter 4",
                        2023,
                        List.of("Keanu Reeves", "Donnie Yen"),
                        LocalDate.of(2023, 3, 22)),
                new MovieInfo(null,
                        "Jason Bourne",
                        2016,
                        List.of("Matt Damon", "Tommy lee"),
                        LocalDate.of(2016, 7, 28))
        );

        repository.saveAll(movieInfos)
                .log()
                .subscribe();
    }

    @Test
    void shouldGetAllMovies() {
        Flux<MovieInfo> allMoviesFlux = repository.findAll();

        StepVerifier.create(allMoviesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }
}