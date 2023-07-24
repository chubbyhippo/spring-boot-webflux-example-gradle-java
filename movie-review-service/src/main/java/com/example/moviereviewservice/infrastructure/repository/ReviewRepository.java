package com.example.moviereviewservice.infrastructure.repository;

import com.example.moviereviewservice.entity.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {

    Flux<Review> findReviewsByMovieInfoId(String movieInfoId);

}
