package com.example.movieinfoservice.interfaces.rest.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class MovieInfoResourceJsonTest {

    @Autowired
    private JacksonTester<MovieInfoResource> json;

    @Test
    @SneakyThrows
    void shouldSerialize() {
        var movieInfoDto = new MovieInfoResource("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        assertThat(json.write(movieInfoDto)).extractingJsonPathStringValue("@.id")
                .satisfies(input -> assertThat(input).isEqualTo(movieInfoDto.id()));
        assertThat(json.write(movieInfoDto)).extractingJsonPathStringValue("@.name")
                .satisfies(input -> assertThat(input).isEqualTo(movieInfoDto.name()));
        assertThat(json.write(movieInfoDto)).extractingJsonPathNumberValue("@.year")
                .satisfies(input -> assertThat(input).isEqualTo(movieInfoDto.year()));
        assertThat(json.write(movieInfoDto)).extractingJsonPathStringValue("@.actors[0]")
                .satisfies(input -> assertThat(input).isEqualTo(movieInfoDto.actors().get(0)));
        assertThat(json.write(movieInfoDto)).extractingJsonPathStringValue("@.actors[1]")
                .satisfies(input -> assertThat(input).isEqualTo(movieInfoDto.actors().get(1)));
        assertThat(json.write(movieInfoDto)).extractingJsonPathStringValue("@.releaseDate")
                .satisfies(input -> assertThat(input).isEqualTo(movieInfoDto.releaseDate().toString()));
    }

    @Test
    @SneakyThrows
    void shouldDeserialize() {
        var content = """
                {
                    "id": "1",
                    "name": "Nobody",
                    "year": 2021,
                    "actors": ["Bob Odenkirk", "Connie Nielsen"],
                    "releaseDate": "2021-04-13"
                }
                """;

        var expected = new MovieInfoResource("1",
                "Nobody",
                2021,
                List.of("Bob Odenkirk", "Connie Nielsen"),
                LocalDate.of(2021, 4, 13));

        assertThat(json.parse(content)).usingRecursiveComparison()
                .satisfies(input -> assertThat(input).isEqualTo(expected));


    }


}