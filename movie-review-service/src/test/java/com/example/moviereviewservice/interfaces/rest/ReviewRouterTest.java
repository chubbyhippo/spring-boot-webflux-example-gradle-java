package com.example.moviereviewservice.interfaces.rest;

import com.example.moviereviewservice.interfaces.rest.dto.ReviewResource;
import com.example.moviereviewservice.domain.entity.Review;
import com.example.moviereviewservice.application.handler.ReviewHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest
@Import(ReviewRouter.class)
class ReviewRouterTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReviewHandler handler;


    @Test
    void shouldAddReview() {
        var serverResponseMono = ServerResponse.status(HttpStatus.CREATED)
                .bodyValue(new ReviewResource("1", "", "da best", 8.9));

        when(handler.addReview(any())).thenReturn(serverResponseMono);

        var review = new Review(null, "", "da best", 8.9);

        client.post()
                .uri("/v1/reviews")
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(ReviewResource.class);

        verify(handler, times(1)).addReview(any());
    }

    @Test
    void shouldReturnBadRequest() {


        var serverResponseMono = ServerResponse.status(HttpStatus.BAD_REQUEST)
                .build();
        when(handler.addReview(any())).thenReturn(serverResponseMono);

        var review = new Review(null, "", "da best", -8.9);

        client.post()
                .uri("/v1/reviews")
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(handler, times(1)).addReview(any());
    }

    @Test
    void shouldGetReviews() {
        Mono<ServerResponse> serverResponseMono = ServerResponse.status(HttpStatus.OK)
                .body(Flux.fromIterable(List.of(new ReviewResource("1", "1", "good", 7.0),
                                new ReviewResource("2", "2", "better", 9.0),
                                new ReviewResource("3", "3", "best", 8.0))),
                        ReviewResource.class);

        when(handler.getReviews(any())).thenReturn(serverResponseMono);

        client.get()
                .uri("/v1/reviews")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ReviewResource.class)
                .hasSize(3);

        verify(handler, times(1)).getReviews(any());
    }

    @Test
    void shouldUpdateReview() {
        var serverResponseMono = ServerResponse.status(HttpStatus.OK)
                .bodyValue(new ReviewResource("1", "", "da best", 0.9));

        when(handler.updateReview(any())).thenReturn(serverResponseMono);

        var id = "1";

        client.put()
                .uri("/v1/reviews/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ReviewResource.class)
                .value(reviewDto -> {
                    var updatedRating = reviewDto.rating();
                    Assertions.assertThat(updatedRating).isEqualTo(0.9);
                });


        verify(handler, times(1)).updateReview(any());
    }

    @Test
    void shouldDeleteReview() {

        when(handler.deleteReview(any())).thenReturn(Mono.empty());
        var id = "1";

        client.delete()
                .uri("/v1/reviews/{id}", id)
                .exchange()
                .expectStatus()
                .isOk();

    }

}