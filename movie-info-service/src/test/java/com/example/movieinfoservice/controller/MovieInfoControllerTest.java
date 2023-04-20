package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.dto.MovieInfoDto;
import com.example.movieinfoservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest
class MovieInfoControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private MovieInfoService service;


    @Test
    void shouldGetMovieInfoDtos() {
        List<MovieInfoDto> movieInfoDtos = List.of(
                new MovieInfoDto("1",
                        "Nobody",
                        2021,
                        List.of("Bob Odenkirk", "Connie Nielsen"),
                        LocalDate.of(2021, 4, 13)),
                new MovieInfoDto("2",
                        "John Wick: Chapter 4",
                        2023,
                        List.of("Keanu Reeves", "Donnie Yen"),
                        LocalDate.of(2023, 3, 22)),
                new MovieInfoDto("3",
                        "Jason Bourne",
                        2016,
                        List.of("Matt Damon", "Tommy lee"),
                        LocalDate.of(2016, 7, 28))
        );
        when(service.getMovieInfos()).thenReturn(Flux.fromIterable(movieInfoDtos));
        var responseBody = client.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfoDto.class)
                .hasSize(3)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody).isNotNull();
    }

    @Test
    void shouldGetMovieInfoById() {

        var id = "1";

        var movieInfoDto = new MovieInfoDto("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        when(service.getMovieInfoById(id)).thenReturn(Mono.just(movieInfoDto));

        client.get()
                .uri("/v1/movieinfos/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfoDto.class);

    }

    @Test
    void shouldGetMovieInfoByYear() {

        var year = 2021 ;

        var movieInfoDto = new MovieInfoDto("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        when(service.getMovieInfosByYear(year)).thenReturn(Flux.just(movieInfoDto));

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/movieinfos")
                        .queryParam("year", year)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfoDto.class)
                .hasSize(1);

    }

    @Test
    void shouldGetMovieInfoByName() {

        var name = "Nobody" ;

        var movieInfoDto = new MovieInfoDto("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        when(service.getMovieInfosByName(name)).thenReturn(Flux.just(movieInfoDto));

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/movieinfos")
                        .queryParam("name", name)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfoDto.class)
                .hasSize(1);

    }

    @Test
    void shouldReturnNotFoundWhenGetMovieInfoByIdReturnMonoEmpty(){
        var id = "1";

        when(service.getMovieInfoById(anyString())).thenReturn(Mono.empty());

        client.get()
                .uri("/v1/movieinfos/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

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

    @Test
    void shouldValidateMovieInfoDtoWhenAdding() {
        var invalidMovieInfoDto = new MovieInfoDto(null,
                "",
                -9999,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        client.post()
                .uri("/v1/movieinfos")
                .bodyValue(invalidMovieInfoDto)
                .exchange()
                .expectStatus()
                .isBadRequest();

    }

    @Test
    void shouldUpdateMovieInfo() {

        var toBeUpdatedMovieInfoDto = new MovieInfoDto("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        var id = "1";
        when(service.updateMovieInfo(toBeUpdatedMovieInfoDto, id))
                .thenReturn(Mono.just(toBeUpdatedMovieInfoDto));

        client.post()
                .uri("/v1/movieinfos/{id}", id)
                .bodyValue(toBeUpdatedMovieInfoDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfoDto.class);
    }

    @Test
    void shouldReturnNotFoundWhenUpdateMovieInfoThatDoesNotExist() {

        var toBeUpdatedMovieInfoDto = new MovieInfoDto("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        var id = "1";
        when(service.updateMovieInfo(toBeUpdatedMovieInfoDto, id))
                .thenReturn(Mono.empty());

        client.post()
                .uri("/v1/movieinfos/{id}", id)
                .bodyValue(toBeUpdatedMovieInfoDto)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void shouldDeleteMovieInfo() {
        var id = "1";
        when(service.deleteMovieInfo(anyString()))
                .thenReturn(Mono.empty());
        client.delete()
                .uri("/v1/movieinfos/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}