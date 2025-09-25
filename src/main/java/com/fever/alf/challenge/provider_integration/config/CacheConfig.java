package com.fever.alf.challenge.provider_integration.config;

import com.fever.alf.challenge.provider_integration.application.GetPlansUseCase;
import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<CachedGetPlansUseCase.Range, List<Plan>> plansCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(500)
                .build();
    }

    /**
     * Override the default GetPlansUseCase to add caching.
     * Marked @Primary so that it is injected wherever GetPlansUseCase is needed.
     */
    @Bean
    @Primary
    public CachedGetPlansUseCase cachedGetPlansUseCase(
            GetPlansUseCase delegate,
            Cache<CachedGetPlansUseCase.Range, List<Plan>> cache) {
        return new CachedGetPlansUseCase(delegate, cache);
    }
}
