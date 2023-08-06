package com.example.movieservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.MockServerClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractTestcontainers {

    @Container
    private static final MockServerContainer mockServerContainer = new MockServerContainer(DockerImageName
            .parse("mockserver/mockserver:5.15.0"));

    public static MockServerClient mockServerClient;

    @DynamicPropertySource
    static void mockServerProperties(DynamicPropertyRegistry registry) {

        mockServerClient = new MockServerClient(mockServerContainer.getHost(),
                mockServerContainer.getServerPort());
        System.out.println(mockServerContainer.getEndpoint());
        registry.add("infrastructure.url.movie-info-service", mockServerContainer::getEndpoint);

    }

    @BeforeEach
    void setUp() {
        mockServerClient.reset();
    }
}
