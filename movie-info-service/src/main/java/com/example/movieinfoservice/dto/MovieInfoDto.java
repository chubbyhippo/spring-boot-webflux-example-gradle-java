package com.example.movieinfoservice.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieInfoDto(
        String id,
        String name,
        Integer year,
        List<String> actors,
        LocalDate releaseDate
) {
}
