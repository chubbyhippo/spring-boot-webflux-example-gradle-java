package com.example.moviereviewservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers(AbstractTestcontainers.class)
public class MovieReviewServiceContainersConfiguration {
}
