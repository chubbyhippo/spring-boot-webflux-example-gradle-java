package com.example.moviereviewservice.router;

import com.example.moviereviewservice.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler handler) {
        return route()
                .GET("/v1/reviews", request -> handler.getReviews())
                .POST("/v1/reviews", handler::addReview)
                .build();
    }
}
