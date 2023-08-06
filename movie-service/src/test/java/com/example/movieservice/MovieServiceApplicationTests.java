package com.example.movieservice;

import com.example.movieservice.config.AbstractTestcontainers;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieServiceApplicationTests extends AbstractTestcontainers {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldGetMovieInfoById() {
        mockServerClient.when(
                        request().withPath("/v1/movieinfos/{id}")
                                .withPathParameter("id", "1"))
                .respond(response("""
                        {
                          	"id": "1",
                           	"name": "Nobody",
                           	"year": 2021,
                           	"actors": [
                           		"Bob Odenkirk",
                           		"Connie Nielsen"
                           	],
                           	"releaseDate": "2021-04-13"
                        }
                        """)
                        .withContentType(MediaType.APPLICATION_JSON));


        webTestClient.get()
                .uri("/{id}", "1")
                .exchange()
                .expectStatus()
                .isOk();

    }

}
