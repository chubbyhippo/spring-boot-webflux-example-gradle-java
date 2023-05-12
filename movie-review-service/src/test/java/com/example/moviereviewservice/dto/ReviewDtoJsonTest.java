package com.example.moviereviewservice.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ReviewDtoJsonTest {

    @Autowired
    private JacksonTester<ReviewDto> json;

    @Test
    @SneakyThrows
    void shouldSerialize() {

        var reviewDto = new ReviewDto("1", "1", "good", 9.0);

        assertThat(json.write(reviewDto)).extractingJsonPathStringValue("@.id")
                .isEqualTo(reviewDto.id());
        assertThat(json.write(reviewDto)).extractingJsonPathStringValue("@.movieInfoId")
                .isEqualTo(reviewDto.movieInfoId());
        assertThat(json.write(reviewDto)).extractingJsonPathStringValue("@.comment")
                .isEqualTo(reviewDto.comment());
        assertThat(json.write(reviewDto)).extractingJsonPathNumberValue("@.rating")
                .isEqualTo(reviewDto.rating());
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

        assertThat(json.parse(content)).usingRecursiveComparison()
                .isEqualTo(new ReviewDto("1",
                        "1",
                        "good",
                        9.0));


    }
}