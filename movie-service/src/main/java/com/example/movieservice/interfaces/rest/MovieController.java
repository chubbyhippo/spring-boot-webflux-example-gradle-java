package com.example.movieservice.interfaces.rest;

import com.example.movieservice.domain.MovieInfoResource;
import com.example.movieservice.infrastucture.service.MovieInfoResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieInfoResourceService movieInfoResourceService;

    @GetMapping("/{id}")
    Mono<MovieInfoResource> getMovieInfoResourceById(@PathVariable String id) {
        return movieInfoResourceService.getMovieInfoById(id);
    }
}
