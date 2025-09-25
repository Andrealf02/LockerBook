package com.fever.alf.challenge.provider_integration.config;

import com.fever.alf.challenge.provider_integration.application.GetPlansUseCase;
import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Decorator around GetPlansUseCase that caches results per date-range.
 */
@Component
@RequiredArgsConstructor
public class CachedGetPlansUseCase {

    private final GetPlansUseCase delegate;
    private final Cache<Range, List<Plan>> cache;

    /**
     * Returns cached plans for the given range, or delegates on cache miss.
     */
    public List<Plan> execute(LocalDateTime from, LocalDateTime to) {
        var key = new Range(from, to);
        return cache.get(key, unused -> delegate.execute(from, to));
    }

    /**
     * Cache key composed of from/to.
     */
    public record Range(LocalDateTime from, LocalDateTime to) {
        public Range {
            Objects.requireNonNull(from, "from must not be null");
            Objects.requireNonNull(to,   "to   must not be null");
        }
    }
}
