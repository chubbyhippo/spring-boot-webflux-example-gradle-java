package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.controller.dto.MovieInfoDto;
import org.springframework.stereotype.Component;

@Component
public class MovieInfoConverter {

    public MovieInfoDto toDto(MovieInfo movieInfo) {
        return new MovieInfoDto(movieInfo.getId(),
                movieInfo.getName(),
                movieInfo.getYear(),
                movieInfo.getActors(),
                movieInfo.getReleaseDate());
    }

    public MovieInfo toDocument(MovieInfoDto movieInfoDto) {
        return new MovieInfo(movieInfoDto.id(),
                movieInfoDto.name(),
                movieInfoDto.year(),
                movieInfoDto.actors(),
                movieInfoDto.releaseDate());
    }
}
