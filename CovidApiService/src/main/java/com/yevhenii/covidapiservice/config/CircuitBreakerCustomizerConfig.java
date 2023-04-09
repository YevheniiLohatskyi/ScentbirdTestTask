package com.yevhenii.covidapiservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerCustomizerConfig {

    @Bean
    Customizer<CircuitBreakerFactory> circuitBreakerFactory() {

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig
            .custom()
            .timeoutDuration(Duration.ofMillis(300))
            .build();

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(5)
            .waitDurationInOpenState(Duration.ofMillis(300))
            .slidingWindowSize(2)
            .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder("config")
            .circuitBreakerConfig(circuitBreakerConfig)
            .timeLimiterConfig(timeLimiterConfig)
            .build());
    }
}
