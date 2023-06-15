package com.example.movieservice.infrastucture.config;

import com.example.movieservice.infrastucture.service.MovieInfoDtoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Bean
    MovieInfoDtoService movieInfoDtoService() {
        var webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .build();
        var factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
        return factory.createClient(MovieInfoDtoService.class);
    }
}
