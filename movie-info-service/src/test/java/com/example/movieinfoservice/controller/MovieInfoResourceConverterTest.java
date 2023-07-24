package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.domain.entity.MovieInfo;
import com.example.movieinfoservice.controller.dto.MovieInfoResource;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MovieInfoResourceConverterTest {

    private final MovieInfoDtoConverter movieInfoDtoConverter = new MovieInfoDtoConverter();
    private final MovieInfo movieInfo = new MovieInfo("1",
            "Nobody",
            2021,
            List.of("Bob Odenkirk", "Connie Nielsen"),
            LocalDate.of(2021, 4, 13));

    private final MovieInfoResource movieInfoResource = new MovieInfoResource("1",
            "Nobody",
            2021,
            List.of("Bob Odenkirk", "Connie Nielsen"),
            LocalDate.of(2021, 4, 13));

    @Test
    void shouldConvertToDto() {

        assertThat(movieInfoDtoConverter.toDto(movieInfo)).isEqualTo(movieInfoResource);

    }

    @Test
    void shouldConvertToDocument() {
        assertThat(movieInfoDtoConverter.toDocument(movieInfoResource)).isEqualTo(movieInfo);
    }
}