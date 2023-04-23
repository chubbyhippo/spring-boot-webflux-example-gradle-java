package com.example.moviereviewservice.repository;

import com.example.moviereviewservice.document.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {
}
