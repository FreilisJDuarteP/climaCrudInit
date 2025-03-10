package com.tiempo.clima.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public Bucket rateLimitBucket() {
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofHours(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}
