package com.example.moviereviewservice.router;

import com.example.moviereviewservice.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler handler) {
        return route()
                .nest(path("/v1/reviews"), builder ->
                        builder.GET("", handler::getReviews)
                                .POST("", handler::addReview)
                                .PUT("/{id}", handler::updateReview)
                                .DELETE("/{id}", handler::deleteReview)
                                .build())
                .build();
    }
}
