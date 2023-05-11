package com.example.moviereviewservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewDto(
        String id,
        @NotBlank
        String movieInfoId,
        String comment,

        @Min(0L)
        Double rating
) {
}
