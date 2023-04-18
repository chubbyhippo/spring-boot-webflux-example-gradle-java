package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.dto.MovieInfoDto;
import com.example.movieinfoservice.service.MovieInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MovieInfoController {

    private final MovieInfoService service;

    @GetMapping("/movieinfos")
    public Flux<MovieInfoDto> getMovieInfos() {
        return service.getMovieInfo();
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<MovieInfoDto> getMovieInfoById(@PathVariable String id) {
        return service.getMovieInfoById(id);
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfoDto> addMovieInfo(@RequestBody @Valid MovieInfoDto movieInfoDto) {
        return service.addMovieInfo(movieInfoDto).log();
    }

    @PostMapping("/movieinfos/{id}")
    public Mono<MovieInfoDto> updateMovieInfoById(@RequestBody MovieInfoDto movieInfoDto, @PathVariable String id) {
        return service.updateMovieInfo(movieInfoDto, id);
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfoById(@PathVariable String id) {
        return service.deleteMovieInfo(id);
    }
}
