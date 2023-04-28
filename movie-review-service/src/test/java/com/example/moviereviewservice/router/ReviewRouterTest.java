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

}