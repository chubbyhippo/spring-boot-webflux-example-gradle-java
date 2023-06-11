package com.example.movieservice.domain;

import java.time.LocalDate;

public record MovieInfoDto(
        String id,
        String name,
        Integer year,
        LocalDate releaseDate
) {
}
