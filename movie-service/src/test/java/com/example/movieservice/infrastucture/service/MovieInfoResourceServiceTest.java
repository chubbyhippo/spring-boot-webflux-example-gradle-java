package com.example.movieservice.infrastucture.service;

import com.example.movieservice.config.AbstractTestcontainers;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
class MovieInfoResourceServiceTest extends AbstractTestcontainers {

    @Autowired
    private MovieInfoResourceService service;

    @Test
    void shouldGetMovieInfoById() {
        var id = "1";

        mockServerClient.when(
                        request().withPath("/v1/movieinfos/{id}")
                                .withPathParameter("id", id))
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

        StepVerifier.create(service.getMovieInfoById(id))
                .expectNextCount(1)
                .verifyComplete();


    }
}