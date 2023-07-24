package com.example.movieservice.infrastucture.service;

import com.example.movieservice.domain.MovieInfoResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface MovieInfoResourceService {

    @GetExchange("/{id}")
    Mono<MovieInfoResource> getMovieInfoById(@PathVariable String id);
}
