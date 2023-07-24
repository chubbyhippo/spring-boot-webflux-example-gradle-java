package com.example.moviereviewservice.exceptionhandler;

import com.example.moviereviewservice.application.exception.ReviewDtoException;
import com.example.moviereviewservice.application.exception.ReviewNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {
    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Exception message is {}, ", ex.getMessage());
        var dataBufferFactory = exchange.getResponse()
                .bufferFactory();
        var errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes());

        if (ex instanceof ReviewDtoException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ReviewNotFoundException) {

            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        }
        return exchange.getResponse()
                .writeWith(Mono.just(errorMessage));
    }
}
