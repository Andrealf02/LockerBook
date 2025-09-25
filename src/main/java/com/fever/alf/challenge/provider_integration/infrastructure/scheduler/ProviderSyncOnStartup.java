package com.fever.alf.challenge.provider_integration.infrastructure.scheduler;

import com.fever.alf.challenge.provider_integration.application.SyncPlansUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Runs a provider sync once at application startup.
 */
@Component
@RequiredArgsConstructor
public class ProviderSyncOnStartup implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(ProviderSyncOnStartup.class);
    private final SyncPlansUseCase syncUseCase;

    @Override
    public void run(ApplicationArguments args) {
        log.info("[ProviderSyncOnStartup] 🚀 Running initial provider sync at startup");
        try {
            syncUseCase.execute();
            log.info("[ProviderSyncOnStartup] ✅ Initial provider sync completed successfully");
        } catch (Exception ex) {
            log.error("[ProviderSyncOnStartup] ❌ Initial provider sync failed", ex);
        }
    }
}
