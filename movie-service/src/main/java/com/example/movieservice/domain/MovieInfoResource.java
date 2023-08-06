package com.example.movieservice.domain;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MovieInfoResource(
        String id,
        String name,
        Integer year,
        List<String> actors,
        LocalDate releaseDate
) {
}
