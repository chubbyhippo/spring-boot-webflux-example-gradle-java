package com.example.moviereviewservice;

import com.example.moviereviewservice.document.Review;
import com.example.moviereviewservice.dto.ReviewDto;
import com.example.moviereviewservice.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieReviewServiceApplicationTests extends AbstractTestcontainers {

    @Autowired
    private ReviewRepository repository;
    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        var reviews = List.of(
                new Review("1", "1", "good", 9.0),
                new Review("2", "2", "better", 8.0),
                new Review("3", "3", "best", 7.0)
        );

        repository.saveAll(reviews)
                .log()
                .blockLast();

    }

    @AfterEach
    void tearDown() {
        repository.deleteAll()
                .block();
    }


    @Test
    void shouldAddReview() {
        var toBeSavedDto = new ReviewDto("", "4", "bad", 1.0);
        client.post()
                .uri("/v1/reviews")
                .bodyValue(toBeSavedDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(ReviewDto.class)
                .value(reviewDto -> assertThat(reviewDto.id())
                        .isNotNull());

    }

    @Test
    void shouldGetReviews() {

        client.get()
                .uri("/v1/reviews")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ReviewDto.class)
                .hasSize(3);

    }

    @Test
    void shouldUpdateReview() {

        var id = "1";
        var toBeUpdated = new ReviewDto("", "4", "bad", 0.0);

        client.put()
                .uri("/v1/reviews/{id}", id)
                .bodyValue(toBeUpdated)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ReviewDto.class)
                .value(reviewDto -> assertThat(reviewDto.rating())
                        .isEqualTo(0.0));

    }

    @Test
    void shouldDeleteReview() {

        var id = "1";
        client.delete()
                .uri("/v1/reviews/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent();

        var count = repository.findAll()
                .count()
                .block();

        assertThat(count).isEqualTo(2L);
    }

}
