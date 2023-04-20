package com.example.movieinfoservice.repository;

import com.example.movieinfoservice.AbstractTestcontainers;
import com.example.movieinfoservice.document.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

@DataMongoTest
class MovieInfoRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private MovieInfoRepository repository;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(
                new MovieInfo("1",
                        "Nobody",
                        2021,
                        List.of("Bob Odenkirk", "Connie Nielsen"),
                        LocalDate.of(2021, 4, 13)),
                new MovieInfo("2",
                        "John Wick: Chapter 4",
                        2023,
                        List.of("Keanu Reeves", "Donnie Yen"),
                        LocalDate.of(2023, 3, 22)),
                new MovieInfo("3",
                        "Jason Bourne",
                        2016,
                        List.of("Matt Damon", "Tommy lee"),
                        LocalDate.of(2016, 7, 28))
        );

        repository.saveAll(movieInfos)
                .log()
                .blockLast();
    }

    @Test
    void shouldGetMovieInfoByYear() {
        var movieInfoByYear = repository.findMovieInfoByYear(2023)
                .log();
        StepVerifier.create(movieInfoByYear)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldGetMovieInfoByName() {
        var movieInfoByName = repository.findMovieInfoByName("Jason Bourne")
                .log();

        StepVerifier.create(movieInfoByName)
                .expectNextCount(1)
                .verifyComplete();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll()
                .block();
    }

}