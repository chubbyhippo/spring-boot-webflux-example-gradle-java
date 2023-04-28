package com.example.moviereviewservice.dto;

public record ReviewDto(
        String id,
        String movieInfoId,
        String comment,
        Double rating
) {
}
