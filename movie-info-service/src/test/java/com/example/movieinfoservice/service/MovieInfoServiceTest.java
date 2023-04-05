package com.example.movieinfoservice.service;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovieInfoServiceTest {

    @Mock
    private MovieInfoRepository repository;

    @InjectMocks
    private MovieInfoService service;

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null,
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));


        Mockito.when(repository.save(Mockito.any(MovieInfo.class)))
                .thenReturn(Mono.just(movieInfo));

        Mono<MovieInfo> movieInfoMono = service.addMovieInfo(movieInfo);

        StepVerifier.create(movieInfoMono)
                .expectNextCount(1)
                .verifyComplete();

    }
}