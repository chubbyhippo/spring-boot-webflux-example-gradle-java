package com.example.movieinfoservice.repository;

import com.example.movieinfoservice.document.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}
