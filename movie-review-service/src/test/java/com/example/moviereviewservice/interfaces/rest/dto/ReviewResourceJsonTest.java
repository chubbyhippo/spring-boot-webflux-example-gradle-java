package com.example.moviereviewservice.interfaces.rest.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ReviewResourceJsonTest {

    @Autowired
    private JacksonTester<ReviewResource> json;

    @Test
    @SneakyThrows
    void shouldSerialize() {

        var reviewDto = new ReviewResource("1", "1", "good", 9.0);

        assertThat(json.write(reviewDto)).extractingJsonPathStringValue("@.id")
                .satisfies(input -> assertThat(input).isEqualTo(reviewDto.id()));
        assertThat(json.write(reviewDto)).extractingJsonPathStringValue("@.movieInfoId")
                .satisfies(input -> assertThat(input).isEqualTo(reviewDto.movieInfoId()));
        assertThat(json.write(reviewDto)).extractingJsonPathStringValue("@.comment")
                .satisfies(input -> assertThat(input).isEqualTo(reviewDto.comment()));
        assertThat(json.write(reviewDto)).extractingJsonPathNumberValue("@.rating")
                .satisfies(input -> assertThat(input).isEqualTo(reviewDto.rating()));
    }

    @Test
    @SneakyThrows
    void shouldDeserialize() {
        var content = """
                {
                    "id": "1",
                    "movieInfoId": "1",
                    "comment": "good",
                    "rating": 9.0
                }
                """;

        var expected = new ReviewResource("1",
                "1",
                "good",
                9.0);
        assertThat(json.parse(content)).usingRecursiveComparison()
                .satisfies(input -> assertThat(input).isEqualTo(expected));

    }
}