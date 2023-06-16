package com.example.movieinfoservice.config;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final MovieInfoRepository repository;

    @Bean
    ApplicationRunner loader() {
        return args -> {

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
        };
    }
}
