package com.fever.alf.challenge.provider_integration.infrastructure.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProviderHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;
    private final String providerUrl;

    public ProviderHealthIndicator(
            RestTemplate restTemplate,
            @Value("${provider.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.providerUrl = baseUrl + "/api/events";
    }

    @Override
    public Health health() {
        try {
            restTemplate.getForObject(providerUrl, String.class);
            return Health.up()
                    .withDetail("providerUrl", providerUrl)
                    .withDetail("status", "Available")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("providerUrl", providerUrl)
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
