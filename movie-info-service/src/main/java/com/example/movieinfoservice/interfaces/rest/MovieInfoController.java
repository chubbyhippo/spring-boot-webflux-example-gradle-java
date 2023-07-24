package com.example.movieinfoservice.interfaces.rest;

import com.example.movieinfoservice.application.service.MovieInfoService;
import com.example.movieinfoservice.interfaces.rest.dto.MovieInfoResource;
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
    private final MovieInfoDtoConverter converter;

    @GetMapping("/movieinfos")
    Flux<MovieInfoResource> getMovieInfos(@RequestParam Optional<Integer> year, @RequestParam Optional<String> name) {

        return year.map(integer -> service.getMovieInfosByYear(integer)
                        .map(converter::toDto))
                .orElseGet(() -> name
                        .map(s -> service.getMovieInfosByName(s)
                                .map(converter::toDto))
                        .orElseGet(() -> service.getMovieInfos()
                                .map(converter::toDto)));

    }

    @GetMapping("/movieinfos/{id}")
    Mono<ResponseEntity<MovieInfoResource>> getMovieInfoById(@PathVariable String id) {
        return service.getMovieInfoById(id)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<MovieInfoResource> addMovieInfo(@RequestBody @Valid MovieInfoResource movieInfoResource) {
        var movieInfo = converter.toDocument(movieInfoResource);
        return service.addMovieInfo(movieInfo)
                .map(converter::toDto);
    }

    @PostMapping("/movieinfos/{id}")
    Mono<ResponseEntity<MovieInfoResource>> updateMovieInfoById(@RequestBody MovieInfoResource movieInfoResource,
                                                                @PathVariable String id) {
        var movieInfo = converter.toDocument(movieInfoResource);
        return service.updateMovieInfo(movieInfo, id)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> deleteMovieInfoById(@PathVariable String id) {
        return service.deleteMovieInfo(id);
    }
}
