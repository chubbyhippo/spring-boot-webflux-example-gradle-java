package com.example.movieservice.infrastucture.config;

import com.example.movieservice.infrastucture.service.MovieInfoResourceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Value("${infrastructure.url.movie-info-service}")
    private String movieInfoServiceUrl;

    @Bean
    MovieInfoResourceService movieInfoDtoService() {
        var webClient = WebClient.builder()
                .baseUrl(movieInfoServiceUrl)
                .build();
        var factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
        return factory.createClient(MovieInfoResourceService.class);
    }
}
