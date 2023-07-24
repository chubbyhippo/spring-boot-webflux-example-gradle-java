package com.example.moviereviewservice.handler;

import com.example.moviereviewservice.entity.Review;
import com.example.moviereviewservice.interfaces.rest.dto.ReviewResource;
import com.example.moviereviewservice.exception.ReviewDtoException;
import com.example.moviereviewservice.exception.ReviewNotFoundException;
import com.example.moviereviewservice.infrastructure.repository.ReviewRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.EntityResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewHandlerTest {

    @Mock
    private ReviewRepository repository;

    @Mock
    private Validator validator;

    @InjectMocks
    private ReviewHandler handler;

    @Test
    @SuppressWarnings("unchecked")
    void shouldAddReview() {

        var requestDto = new ReviewResource(null, "1", "da best", 8.9);
        var savedReview = new Review("1", "1", "da best", 8.9);
        var responseDto = new ReviewResource("1", "1", "da best", 8.9);

        when(repository.save(any())).thenReturn(Mono.just(savedReview));
        when(validator.validate(any(ReviewResource.class))).thenReturn(new HashSet<>());
        var request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .body(Mono.just(requestDto));

        var serverResponseMono = handler.addReview(request);
        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    var responseEntity = (EntityResponse<ReviewResource>) serverResponse;
                    var entity = responseEntity.entity();

                    assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(entity).isEqualTo(responseDto);

                })
                .verifyComplete();

        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldThrowValidationExceptionWhenAddingInvalidReviewDto() {
        var requestDto = new ReviewResource(null, "1", "da best", 8.9);

        when(validator.validate(any(ReviewResource.class))).thenThrow(ReviewDtoException.class);
        var request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .body(Mono.just(requestDto));

        var serverResponseMono = handler.addReview(request);
        StepVerifier.create(serverResponseMono)
                .expectError(ReviewDtoException.class)
                .verify();

    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldGetReviews() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(
                List.of(new Review("1", "1", "good", 7.0),
                        new Review("2", "2", "better", 9.0),
                        new Review("3", "3", "best", 8.0))));

        var request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .build();

        var serverResponseMono = handler.getReviews(request);

        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    var responseEntity = (EntityResponse<Flux<ReviewResource>>) serverResponse;
                    var entity = responseEntity.entity();

                    assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.OK);
                    StepVerifier.create(entity)
                            .expectNextCount(3)
                            .verifyComplete();

                })
                .verifyComplete();

    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldGetReviewsByMovieInfoId() {
        var movieInfoId = "1";

        when(repository.findReviewsByMovieInfoId(movieInfoId))
                .thenReturn(Flux.fromIterable(List.of(new Review("1", "1", "good", 9.0),
                        new Review("2", "1", "bad", 0.0))));

        var request = MockServerRequest.builder()
                .queryParam("movieInfoId", movieInfoId)
                .method(HttpMethod.GET)
                .build();

        var serverResponseMono = handler.getReviews(request);

        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    var responseEntity = (EntityResponse<Flux<ReviewResource>>) serverResponse;
                    var entity = responseEntity.entity();

                    assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.OK);
                    StepVerifier.create(entity)
                            .expectNextCount(2)
                            .verifyComplete();

                })
                .verifyComplete();

    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldUpdateReview() {
        var id = "1";

        when(repository.findById(id))
                .thenReturn(Mono.just(new Review("1", "1", "good", 9.0)));


        var requestDto = new ReviewResource("1", "1", "the best!!", 9.0);
        var responseDto = new ReviewResource("1", "1", "the best!!", 9.0);

        var request = MockServerRequest.builder()
                .pathVariable("id", id)
                .method(HttpMethod.POST)
                .body(Mono.just(requestDto));

        var savedReview = new Review("1", "1", "the best!!", 9.0);
        when(repository.save(any())).thenReturn(Mono.just(savedReview));

        var serverResponseMono = handler.updateReview(request);

        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    var responseEntity = (EntityResponse<ReviewResource>) serverResponse;
                    var entity = responseEntity.entity();

                    assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(entity).isEqualTo(responseDto);

                })
                .verifyComplete();

        verify(repository, times(1)).save(any());

    }

    @Test
    void shouldThrowReviewNotFoundException() {
        var id = "1";
        when(repository.findById(id))
                .thenReturn(Mono.error(new ReviewNotFoundException("Review not found for the given id " + id)));

        var requestDto = new ReviewResource("1", "1", "the best!!", 9.0);

        var request = MockServerRequest.builder()
                .pathVariable("id", id)
                .method(HttpMethod.POST)
                .body(Mono.just(requestDto));

        var serverResponseMono = handler.updateReview(request);

        StepVerifier.create(serverResponseMono)
                .expectError(ReviewNotFoundException.class)
                .verify();
    }

    @Test
    void shouldDeleteReview() {
        var id = "1";

        when(repository.findById(id))
                .thenReturn(Mono.just(new Review("1", "1", "good", 9.0)));
        when(repository.deleteById(id))
                .thenReturn(Mono.empty());

        var request = MockServerRequest.builder()
                .pathVariable("id", id)
                .method(HttpMethod.POST)
                .build();

        var serverResponseMono = handler.deleteReview(request);


        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    var httpStatusCode = serverResponse.statusCode();
                    assertThat(httpStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
                })
                .verifyComplete();


    }


}