package com.example.movieinfoservice.repository;

import com.example.movieinfoservice.document.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
    Flux<MovieInfo> findMovieInfoByYear(Integer year);

    Flux<MovieInfo> findMovieInfoByName(String name);
}
