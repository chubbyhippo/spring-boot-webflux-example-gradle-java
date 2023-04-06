package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.dto.MovieInfoDto;
import com.example.movieinfoservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest
class MovieInfoControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private MovieInfoService service;

    @Test
    void shouldAddMovieInfo() {

        var toBesaveMovieInfoDto = new MovieInfoDto(null,
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        var result = new MovieInfoDto("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        when(service.addMovieInfo(toBesaveMovieInfoDto))
                .thenReturn(Mono.just(result));

        StepVerifier.create(service.addMovieInfo(toBesaveMovieInfoDto))
                .expectNextCount(1)
                .verifyComplete();

        var responseBody = client.post()
                .uri("/v1/movieinfos")
                .bodyValue(toBesaveMovieInfoDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfoDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody)
                .isEqualTo(result);


    }
}