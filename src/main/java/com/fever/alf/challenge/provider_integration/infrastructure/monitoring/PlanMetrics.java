package com.fever.alf.challenge.provider_integration.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class PlanMetrics {
    private final MeterRegistry registry;

    public PlanMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordPlansFetched(int count) {
        registry.counter("plans.fetched.count").increment(count);
    }

    public void recordProviderLatency(long milliseconds) {
        registry.timer("provider.request.latency").record(java.time.Duration.ofMillis(milliseconds));
    }

    public void incrementCacheHits() {
        registry.counter("cache.hits").increment();
    }

    public void incrementCacheMisses() {
        registry.counter("cache.misses").increment();
    }
}
