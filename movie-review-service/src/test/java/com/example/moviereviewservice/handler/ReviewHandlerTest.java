package com.example.moviereviewservice.handler;

import com.example.moviereviewservice.document.Review;
import com.example.moviereviewservice.repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewHandlerTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewHandler handler;

    @Test
    void shouldAddReview() {

        var review = new Review(null, "", "da best", 8.9);
        var savedReview = new Review("1", "", "da best", 8.9);

        when(repository.save(review)).thenReturn(Mono.just(savedReview));
        var request = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .body(Mono.just(review));

        var serverResponseMono = handler.addReview(request);
        StepVerifier.create(serverResponseMono)
                .consumeNextWith(serverResponse -> {
                    Assertions.assertThat(
                            serverResponse.statusCode()
                    ).isEqualTo(HttpStatus.CREATED);
                })
                .verifyComplete();

        verify(repository, times(1)).save(review);

    }
}