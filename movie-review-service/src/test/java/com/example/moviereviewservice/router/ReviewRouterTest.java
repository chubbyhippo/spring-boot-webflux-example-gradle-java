package com.example.moviereviewservice.router;

import com.example.moviereviewservice.document.Review;
import com.example.moviereviewservice.dto.ReviewDto;
import com.example.moviereviewservice.handler.ReviewHandler;
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
@Import({ReviewRouter.class, ReviewHandler.class})
class ReviewRouterTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReviewHandler handler;


    @Test
    void shouldAddReview() {
        var serverResponseMono = ServerResponse.status(HttpStatus.CREATED)
                .bodyValue(new ReviewDto("1", "", "da best", 8.9));

        when(handler.addReview(any())).thenReturn(serverResponseMono);

        var review = new Review(null, "", "da best", 8.9);

        client.post()
                .uri("/v1/reviews")
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(ReviewDto.class);

        verify(handler, times(1)).addReview(any());
    }

    @Test
    void shouldGetReviews() {
        Mono<ServerResponse> serverResponseMono = ServerResponse.status(HttpStatus.OK)
                .body(Flux.fromIterable(List.of(new ReviewDto("1", "1", "good", 7.0),
                                new ReviewDto("2", "2", "better", 9.0),
                                new ReviewDto("3", "3", "best", 8.0))),
                        ReviewDto.class);

        when(handler.getReviews()).thenReturn(serverResponseMono);

        client.get()
                .uri("/v1/reviews")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ReviewDto.class)
                .hasSize(3);

        verify(handler, times(1)).getReviews();
    }

}