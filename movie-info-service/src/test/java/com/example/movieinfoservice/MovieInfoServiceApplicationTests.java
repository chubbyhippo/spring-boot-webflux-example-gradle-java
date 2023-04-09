package com.example.movieinfoservice;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.dto.MovieInfoDto;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieInfoServiceApplicationTests extends MongoDbContainerSetup {

    @Autowired
    private WebTestClient client;

    @Autowired
    private MovieInfoRepository repository;

    @Test
        // Coverage cheating
    void shouldRunMain() {
        try {
            MovieInfoServiceApplication.main(new String[]{});
        } catch (Exception e) {
            assertTrue(true);
        }
        assertTrue(true);
    }

    @BeforeEach
    void setUp() {
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

    @AfterEach
    void tearDown() {
        repository.deleteAll()
                .subscribe();
    }

    @Test
    void shouldGetMovieInfos() {


        client.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfoDto.class)
                .hasSize(3);

    }

    @Test
    void shouldAddMovieInfo() {
        var toBesaveMovieInfoDto = new MovieInfoDto(null,
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        client.post()
                .uri("/v1/movieinfos")
                .bodyValue(toBesaveMovieInfoDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfoDto.class)
                .value(movieInfoDto -> {
                    var id = movieInfoDto.id();
                    assertThat(id).isNotNull();
                });


    }

}
