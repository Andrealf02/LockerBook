package com.fever.alf.challenge.provider_integration.infrastructure.scheduler;

import com.fever.alf.challenge.provider_integration.application.SyncPlansUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job that triggers periodic synchronization
 * of plans from the external provider.
 */
@Component
@RequiredArgsConstructor
public class ProviderSyncJob {

    private static final Logger log = LoggerFactory.getLogger(ProviderSyncJob.class);
    private final SyncPlansUseCase syncUseCase;

    /**
     * Executes the sync use case at a fixed rate.
     * Logs start, success, and errors without throwing.
     */
    @Scheduled(fixedRateString = "${sync.rate.ms}")
    public void sync() {
        log.info("[ProviderSyncJob] 🔄 Starting provider sync job");
        try {
            syncUseCase.execute();
            log.info("[ProviderSyncJob] ✅ Provider sync job completed successfully");
        } catch (Exception ex) {
            log.error("[ProviderSyncJob] ❌ Provider sync job failed", ex);
        }
    }
}
