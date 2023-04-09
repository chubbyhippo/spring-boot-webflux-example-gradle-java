package com.example.movieinfoservice.service;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.dto.MovieInfoDto;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieInfoServiceTest {

    @Mock
    private MovieInfoRepository repository;

    @InjectMocks
    private MovieInfoService service;

    private final List<MovieInfo> movieInfos = List.of(
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

    @Test
    void shouldGetMoviesInfos() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(movieInfos));

        var movieInfosFlux = service.getMovieInfo();

        StepVerifier.create(movieInfosFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void shouldGetMovieInfoById() {

        var id = "1";
        when(repository.findById(id)).thenReturn(Mono.just(movieInfos.get(1)));

        var movieInfoMono = service.getMovieInfoById(id);

        StepVerifier.create(movieInfoMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldAddMovieInfo() {
        var movieInfo = new MovieInfo(null,
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        var toBesaveMovieInfoDto = new MovieInfoDto(null,
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        when(repository.save(any(MovieInfo.class)))
                .thenReturn(Mono.just(movieInfo));

        var movieInfoMono = service.addMovieInfo(toBesaveMovieInfoDto);

        StepVerifier.create(movieInfoMono)
                .expectNextCount(1)
                .verifyComplete();

    }

}