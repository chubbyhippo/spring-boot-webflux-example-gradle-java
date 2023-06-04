package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.dto.MovieInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MovieInfoConverterTest {

    private final MovieInfoConverter movieInfoConverter = new MovieInfoConverter();
    private final MovieInfo movieInfo = new MovieInfo("1",
            "Nobody",
            2021,
            List.of("Bob Odenkirk", "Connie Nielsen"),
            LocalDate.of(2021, 4, 13));

    private final MovieInfoDto movieInfoDto = new MovieInfoDto("1",
            "Nobody",
            2021,
            List.of("Bob Odenkirk", "Connie Nielsen"),
            LocalDate.of(2021, 4, 13));

    @Test
    void shouldConvertToDto() {

        assertThat(movieInfoConverter.toDto(movieInfo)).isEqualTo(movieInfoDto);

    }

    @Test
    void shouldConvertToDocument() {
        assertThat(movieInfoConverter.toDocument(movieInfoDto)).isEqualTo(movieInfo);
    }
}