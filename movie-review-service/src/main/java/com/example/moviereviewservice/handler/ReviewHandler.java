package com.example.moviereviewservice.handler;

import com.example.moviereviewservice.document.Review;
import com.example.moviereviewservice.dto.ReviewDto;
import com.example.moviereviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReviewHandler {

    private final ReviewRepository repository;

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(ReviewDto.class)
                .map(reviewDto -> new Review(reviewDto.id(),
                        reviewDto.movieInfoId(),
                        reviewDto.comment(),
                        reviewDto.rating()))
                .flatMap(repository::save)
                .flatMap(review -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(new ReviewDto(review.getId(),
                                review.getMovieInfoId(),
                                review.getComment(),
                                review.getRating())));
    }

    public Mono<ServerResponse> getReviews() {


        Flux<ReviewDto> reviewDtoFlux = repository.findAll()
                .map(review -> new ReviewDto(review.getId(),
                        review.getMovieInfoId(),
                        review.getComment(),
                        review.getRating()));
        return ServerResponse.ok()
                .body(reviewDtoFlux, Review.class);
    }
}
