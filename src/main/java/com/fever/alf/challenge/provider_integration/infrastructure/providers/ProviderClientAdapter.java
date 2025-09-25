package com.fever.alf.challenge.provider_integration.infrastructure.providers;


import com.fever.alf.challenge.provider_integration.infrastructure.exceptions.ProviderException;
import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.port.FetchPlansFromProviderPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProviderClientAdapter implements FetchPlansFromProviderPort {
    private static final Logger log = LoggerFactory.getLogger(ProviderClientAdapter.class);
    private final WebClient providerWebClient;

    @Override
    @Retry(name = "provider")
    @CircuitBreaker(name = "provider", fallbackMethod = "onError")
    public List<Plan> fetchAllPlans() {
        log.info("[ProviderClientAdapter] Fetching plans from external provider");
        String xml = null;
        try {
            xml = providerWebClient.get()
                    .uri("/api/events")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("[ProviderClientAdapter] Received XML from provider ({} bytes)", xml != null ? xml.length() : 0);
        } catch (Exception ex) {
            log.error("[ProviderClientAdapter] Error fetching XML from provider", ex);
            throw new ProviderException("Error fetching XML from provider", ex);
        }
        if (xml == null || xml.isBlank()) {
            log.warn("[ProviderClientAdapter] Empty XML payload from provider");
            throw new ProviderException("Empty XML payload");
        }
        try {
            List<Plan> plans = XmlPlanParser.parse(xml);
            log.info("[ProviderClientAdapter] Parsed {} plans from provider XML", plans.size());
            return plans;
        } catch (Exception ex) {
            log.error("[ProviderClientAdapter] Error parsing provider XML", ex);
            throw new ProviderException("Error parsing provider XML", ex);
        }
    }

    @SuppressWarnings("unused")
    private List<Plan> onError(Throwable ex) {
        log.error("Provider unavailable, returning empty list", ex);
        return List.of();
    }
}
