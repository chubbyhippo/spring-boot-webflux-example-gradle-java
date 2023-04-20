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

    @Test
    void shouldGetMoviesInfos() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(movieInfos));

        var movieInfosFlux = service.getMovieInfos();

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
    void shouldGetMovieInfoByYear() {
        var year = 2021;
        when(repository.findMovieInfoByYear(year)).thenReturn(Flux.just(movieInfos.get(1)));

        var movieInfosByYear = service.getMovieInfosByYear(year);

        StepVerifier.create(movieInfosByYear)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldGetMovieInfoByName() {
        var name = "Jason Bourne";
        when(repository.findMovieInfoByName(name)).thenReturn(Flux.just(movieInfos.get(2)));

        var movieInfosByYear = service.getMovieInfosByName(name);

        StepVerifier.create(movieInfosByYear)
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

    @Test
    void shouldUpdateMovieInfo() {
        var movieInfo = new MovieInfo("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        var id = "1";
        when(repository.findById(id)).thenReturn(Mono.just(movieInfo));

        var toBeUpdatedMovieInfoDto = new MovieInfoDto("1",
                "Nobody",
                2021,
                List.of("Foo", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));
        var toBeSavedMovieInfo = new MovieInfo("1",
                "Nobody",
                2021,
                List.of("Foo", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        when(repository.save(toBeSavedMovieInfo))
                .thenReturn(Mono.just(toBeSavedMovieInfo));

        var movieInfoMono = service.updateMovieInfo(toBeUpdatedMovieInfoDto, id);

        StepVerifier.create(movieInfoMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    void shouldDeleteMovieInfo() {

        when(repository.deleteById(any(String.class)))
                .thenReturn(Mono.empty());
        var id = "1";
        var voidMono = service.deleteMovieInfo(id);

        StepVerifier.create(voidMono)
                .expectNextCount(0)
                .verifyComplete();

    }
}