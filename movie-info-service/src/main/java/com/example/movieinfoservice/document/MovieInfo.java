package com.example.movieinfoservice.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document
public record MovieInfo(
        @Id
        String id,
        String name,
        Integer year,
        List<String> cast,
        LocalDate releaseDate
) {
}
