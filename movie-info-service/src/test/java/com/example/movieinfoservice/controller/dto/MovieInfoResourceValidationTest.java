package com.example.movieinfoservice.controller.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MovieInfoResourceValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of("1",
                        "",
                        2021,
                        List.of("Bob Odenkirk", "Connie Nielsen"),
                        LocalDate.of(2021, 4, 13),
                        1,
                        "must not be blank"),
                Arguments.of("1",
                        "Nobody",
                        null,
                        List.of("Bob Odenkirk", "Connie Nielsen"),
                        LocalDate.of(2021, 4, 13),
                        1,
                        "must not be null"),
                Arguments.of("1",
                        "Nobody",
                        -9999,
                        List.of("Bob Odenkirk", "Connie Nielsen"),
                        LocalDate.of(2021, 4, 13),
                        1,
                        "must be greater than 0"),
                Arguments.of("1",
                        "Nobody",
                        2021,
                        List.of("", "Connie Nielsen"),
                        LocalDate.of(2021, 4, 13),
                        1,
                        "must not be blank")
        );
    }

    @ParameterizedTest
    @MethodSource("generateData")
    void shouldValidateFields(String id,
                              String name,
                              Integer year,
                              List<String> actors,
                              LocalDate releaseDate,
                              int expectedViolations,
                              String errorMessage) {

        var movieInfoDto = new MovieInfoResource(id, name, year, actors, releaseDate);
        var actualViolations = validator.validate(movieInfoDto);

        assertThat(actualViolations).hasSize(expectedViolations);
        assertThat(actualViolations.iterator().next().getMessage()).isEqualTo(errorMessage);


    }


}