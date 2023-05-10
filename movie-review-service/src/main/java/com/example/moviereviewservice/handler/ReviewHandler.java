package com.example.moviereviewservice.handler;

import com.example.moviereviewservice.document.Review;
import com.example.moviereviewservice.dto.ReviewDto;
import com.example.moviereviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewHandler {

    private final ReviewRepository repository;

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(ReviewDto.class)
                .map(reviewDto -> new Review(null,
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

    public Mono<ServerResponse> getReviews(ServerRequest request) {
        var movieInfoIdOptional = request.queryParam("movieInfoId");

        Flux<ReviewDto> reviewDtoFlux;
        reviewDtoFlux = movieInfoIdOptional.map(s -> repository.findReviewsByMovieInfoId(s)
                .map(review -> new ReviewDto(review.getId(),
                        review.getMovieInfoId(),
                        review.getComment(),
                        review.getRating()))).orElseGet(() -> repository.findAll()
                .map(review -> new ReviewDto(review.getId(),
                        review.getMovieInfoId(),
                        review.getComment(),
                        review.getRating())));
        return ServerResponse.ok()
                .body(reviewDtoFlux, Review.class);


    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        var id = request.pathVariable("id");
        var existingReview = repository.findById(id);

        return existingReview.flatMap(review -> request.bodyToMono(ReviewDto.class)
                .map(reviewDto -> new Review(review.getId(),
                        reviewDto.movieInfoId(),
                        reviewDto.comment(),
                        reviewDto.rating()))
                .flatMap(repository::save)
                .flatMap(savedReview -> ServerResponse.ok()
                        .bodyValue(new ReviewDto(savedReview.getId(),
                                savedReview.getMovieInfoId(),
                                savedReview.getComment(),
                                savedReview.getRating()))));
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {

        var id = request.pathVariable("id");
        var existingReview = repository.findById(id);

        return existingReview
                .flatMap(review -> repository.deleteById(id))
                .then(Mono.defer(() -> ServerResponse.noContent()
                        .build()));
    }

}
