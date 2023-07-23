package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.controller.dto.MovieInfoResource;
import org.springframework.stereotype.Component;

@Component
public class MovieInfoDtoConverter {

    public MovieInfoResource toDto(MovieInfo movieInfo) {
        return new MovieInfoResource(movieInfo.getId(),
                movieInfo.getName(),
                movieInfo.getYear(),
                movieInfo.getActors(),
                movieInfo.getReleaseDate());
    }

    public MovieInfo toDocument(MovieInfoResource movieInfoResource) {
        return new MovieInfo(movieInfoResource.id(),
                movieInfoResource.name(),
                movieInfoResource.year(),
                movieInfoResource.actors(),
                movieInfoResource.releaseDate());
    }
}
