package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.dto.MovieInfoDto;
import com.example.movieinfoservice.service.MovieInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MovieInfoController {

    private final MovieInfoService service;
    private final MovieInfoConverter converter;

    @GetMapping("/movieinfos")
    public Flux<MovieInfoDto> getMovieInfos(@RequestParam Optional<Integer> year, @RequestParam Optional<String> name) {
        Flux<MovieInfoDto> movieInfoDtos;

        if (year.isPresent()) {
            return service.getMovieInfosByYear(year.get());
        } else if (name.isPresent()) {
            return service.getMovieInfosByName(name.get());
        } else {
            movieInfoDtos = service.getMovieInfos()
                    .map(converter::toDto);
        }

        return movieInfoDtos;
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfoDto>> getMovieInfoById(@PathVariable String id) {
        return service.getMovieInfoById(id)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfoDto> addMovieInfo(@RequestBody @Valid MovieInfoDto movieInfoDto) {
        return service.addMovieInfo(movieInfoDto);
    }

    @PostMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfoDto>> updateMovieInfoById(@RequestBody MovieInfoDto movieInfoDto,
                                                                  @PathVariable String id) {
        return service.updateMovieInfo(movieInfoDto, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfoById(@PathVariable String id) {
        return service.deleteMovieInfo(id);
    }
}
