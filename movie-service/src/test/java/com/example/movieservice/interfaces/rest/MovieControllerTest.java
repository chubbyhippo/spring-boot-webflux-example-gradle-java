package com.example.movieservice.interfaces.rest;

import com.example.movieservice.domain.MovieInfoResource;
import com.example.movieservice.infrastucture.service.MovieInfoResourceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest
class MovieControllerTest {

    @MockBean
    private MovieInfoResourceService service;
    @Autowired
    private WebTestClient client;

    @Test
    void shouldGetMovieInfoResourceById() {
        var id = "1";

        var movieInfo = new MovieInfoResource("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        when(service.getMovieInfoById(id)).thenReturn(Mono.just(movieInfo));

        client.get()
                .uri("/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfoResource.class);
    }
}