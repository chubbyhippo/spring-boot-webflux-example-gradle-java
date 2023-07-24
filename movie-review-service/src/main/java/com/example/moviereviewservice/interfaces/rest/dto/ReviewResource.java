package com.example.moviereviewservice.interfaces.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewResource(
        String id,
        @NotBlank
        String movieInfoId,
        String comment,

        @Min(0L)
        Double rating
) {
}
