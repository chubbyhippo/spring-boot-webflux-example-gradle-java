package com.example.movieservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.MockServerClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractTestcontainers {

    private static final MockServerContainer mockServerContainer = new MockServerContainer(DockerImageName
            .parse("mockserver/mockserver:5.15.0"));

    public static MockServerClient mockServerClient;

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {

        mockServerClient = new MockServerClient(mockServerContainer.getHost(),
                mockServerContainer.getServerPort());
        registry.add("", mockServerContainer::getEndpoint);

    }

    @BeforeEach
    void setUp() {
        mockServerClient.reset();
    }
}
