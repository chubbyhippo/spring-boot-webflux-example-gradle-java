package com.example.moviereviewservice.handler;

import com.example.moviereviewservice.document.Review;
import com.example.moviereviewservice.dto.ReviewDto;
import com.example.moviereviewservice.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewHandlerTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewHandler handler;

    @SuppressWarnings("unchecked")
    @Test
    void shouldAddReview() {

        var requestDto = new ReviewDto("1", "", "da best", 8.9);
        var savedReview = new Review("1", "", "da best", 8.9);
        var responseDto = new ReviewDto("1", "", "da best", 8.9);

        when(repository.save(any())).thenReturn(Mono.just(savedReview));
        var request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .body(Mono.just(requestDto));

        var serverResponseMono = handler.addReview(request);
        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    var responseEntity = (EntityResponse<ReviewDto>) serverResponse;
                    var entity = responseEntity.entity();

                    assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(entity).isEqualTo(responseDto);

                })
                .verifyComplete();

        verify(repository, times(1)).save(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetReviews() {
        when(repository.findAll()).thenReturn(Flux.fromIterable(
                List.of(new Review("1", "1", "good", 7.0),
                        new Review("2", "2", "better", 9.0),
                        new Review("3", "3", "best", 8.0))));

        var serverResponseMono = handler.getReviews();

        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    var responseEntity = (EntityResponse<Flux<ReviewDto>>) serverResponse;
                    var entity = responseEntity.entity();

                    assertThat(serverResponse.statusCode()).isEqualTo(HttpStatus.OK);
                    StepVerifier.create(entity)
                            .expectNextCount(3)
                            .verifyComplete();

                })
                .verifyComplete();

    }
}