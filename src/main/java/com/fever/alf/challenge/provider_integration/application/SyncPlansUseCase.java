package com.fever.alf.challenge.provider_integration.application;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.port.FetchPlansFromProviderPort;
import com.fever.alf.challenge.provider_integration.domain.port.PlanRepositoryPort;
import io.micrometer.core.annotation.Timed;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Periodically synchronizes all plans from the external provider.
 *
 * This method is idempotent: existing plans will be updated,
 * new ones inserted, and offline plans simply omitted.
 */
@Service
@RequiredArgsConstructor
public class SyncPlansUseCase {

    private static final Logger log = LoggerFactory.getLogger(SyncPlansUseCase.class);

    private final FetchPlansFromProviderPort provider;
    private final PlanRepositoryPort          repository;

    /**
     * Fetches all plans, filters by sellMode="online", and upserts each one.
     * Runs as a single transaction for bulk consistency.
     */
    @Timed(value = "sync.plans.execution", description = "Time taken to sync plans from provider")
    @Transactional
    public void execute() {
        log.info("🔄 Starting plans sync");

        List<Plan> allPlans;
        try {
            allPlans = provider.fetchAllPlans();
            log.debug("Fetched {} plans from provider", allPlans.size());
        } catch (Exception ex) {
            log.error("❌ Unable to fetch plans from provider, skipping sync", ex);
            return;
        }

        // Filter & upsert, isolating errors per plan
        allPlans.stream()
                .filter(p -> "online".equalsIgnoreCase(p.getSellMode()))
                .forEach(plan -> {
                    try {
                        repository.saveOrUpdate(plan);
                        log.debug("Upserted plan id={}", plan.getId());
                    } catch (Exception ex) {
                        log.error("⚠️ Failed to upsert plan id={}", plan.getId(), ex);
                    }
                });

        log.info("✅ Finished plans sync");
    }
}
