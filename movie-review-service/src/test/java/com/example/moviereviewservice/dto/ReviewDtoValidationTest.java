package com.example.moviereviewservice.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewDtoValidationTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(null,
                        "",
                        "the best",
                        5.0,
                        1,
                        "must not be blank"),
                Arguments.of(null,
                        "1",
                        "the best",
                        -1.0,
                        1,
                        "must be greater than or equal to 0")
        );
    }


    @ParameterizedTest
    @MethodSource("generateData")
    void shouldValidateFields(String id,
                              String movieInfoId,
                              String comment,
                              Double rating,
                              int expectedViolations,
                              String errorMessage) {

        var reviewDto = new ReviewDto(id, movieInfoId, comment, rating);
        var actualViolations = validator.validate(reviewDto);

        assertThat(actualViolations).hasSize(expectedViolations);
        assertThat(actualViolations.iterator().next().getMessage()).isEqualTo(errorMessage);


    }
}