package com.example.movieservice.infrastucture.service;

import com.example.movieservice.domain.MovieInfoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface MovieInfoDtoService {

    @GetExchange("/{id}")
    Mono<MovieInfoDto> getMovieInfoById(@PathVariable String id);
}
