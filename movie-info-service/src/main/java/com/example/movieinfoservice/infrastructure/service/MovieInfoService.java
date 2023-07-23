package com.example.movieinfoservice.infrastructure.service;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.infrastructure.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieInfoService {

    private final MovieInfoRepository repository;

    public Flux<MovieInfo> getMovieInfos() {
        return repository.findAll();

    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return repository.findById(id);
    }

    public Flux<MovieInfo> getMovieInfosByYear(int year) {
        return repository.findMovieInfoByYear(year);
    }

    public Flux<MovieInfo> getMovieInfosByName(String name) {
        return repository.findMovieInfoByName(name);
    }

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return repository.save(movieInfo);
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo movieInfo, String id) {

        return repository.findById(id)
                .flatMap(foundMovieInfo -> {
                    foundMovieInfo.setName(movieInfo.getName());
                    foundMovieInfo.setYear(movieInfo.getYear());
                    foundMovieInfo.setActors(movieInfo.getActors());
                    foundMovieInfo.setReleaseDate(movieInfo.getReleaseDate());

                    return repository.save(foundMovieInfo);

                });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return repository.deleteById(id);
    }
}
