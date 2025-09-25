package com.fever.alf.challenge.provider_integration.infrastructure.providers;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "provider")
public class ProviderProperties {
    private String baseUrl;
    private Duration requestTimeout = Duration.ofSeconds(5);
}
