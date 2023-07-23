package com.example.movieinfoservice;

import com.example.movieinfoservice.config.AbstractTestcontainers;
import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.controller.dto.MovieInfoResource;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieInfoServiceApplicationTests extends AbstractTestcontainers {

    @Autowired
    private WebTestClient client;

    @Autowired
    private MovieInfoRepository repository;

    @Test
    void shouldRunMain() {
        assertThatNoException().isThrownBy(() -> TestMovieInfoServiceApplication.main(new String[]{}));

    }


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

    @AfterEach
    void tearDown() {
        repository.deleteAll()
                .block();
    }

    @Test
    void shouldGetMovieInfos() {

        client.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfoResource.class)
                .hasSize(3);

    }

    @Test
    void shouldGetMovieInfoById() {

        client.get()
                .uri("/v1/movieinfos/{id}", "1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfoResource.class);

    }

    @Test
    void shouldGetMovieInfoByYear() {

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/movieinfos")
                        .queryParam("year", 2021)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfoResource.class)
                .hasSize(1);

    }

    @Test
    void shouldGetMovieInfoByName() {

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/movieinfos")
                        .queryParam("name", "Jason Bourne")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfoResource.class)
                .hasSize(1);

    }

    @Test
    void shouldAddMovieInfo() {
        var toBeSavedMovieInfoDto = new MovieInfoResource(null,
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        client.post()
                .uri("/v1/movieinfos")
                .bodyValue(toBeSavedMovieInfoDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfoResource.class)
                .value(movieInfoDto -> {
                    var id = movieInfoDto.id();
                    assertThat(id).isNotNull();
                });
    }

    @Test
    void shouldUpdateMovieInfo() {

        var toBeUpdatedMovieInfoDto = new MovieInfoResource(null,
                "Nobody",
                2021,
                List.of("Foo", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));
        var id = "1";
        client.post()
                .uri("/v1/movieinfos/{id}", id)
                .bodyValue(toBeUpdatedMovieInfoDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.actors[0]")
                .isEqualTo("Foo");

    }

    @Test
    void shouldDeleteMovieInfo() {
        var id = "1";

        client.delete()
                .uri("/v1/movieinfos/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent();

        var count = repository.findAll()
                .count();

        assertThat(count.block()).isEqualTo(2L);
    }

}
