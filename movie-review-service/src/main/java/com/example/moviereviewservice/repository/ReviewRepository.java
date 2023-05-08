package com.example.moviereviewservice.repository;

import com.example.moviereviewservice.document.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {

    Flux<Review> findReviewsByMovieInfoId(String movieInfoId);

}
