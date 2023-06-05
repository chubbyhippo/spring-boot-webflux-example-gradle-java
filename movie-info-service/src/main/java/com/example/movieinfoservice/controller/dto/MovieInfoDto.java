package com.example.movieinfoservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record MovieInfoDto(
        String id,
        @NotBlank
        String name,
        @NotNull
        @Positive
        Integer year,
        List<@NotBlank String> actors,
        LocalDate releaseDate
) {
}
