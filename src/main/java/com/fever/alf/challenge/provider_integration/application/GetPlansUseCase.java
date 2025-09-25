package com.fever.alf.challenge.provider_integration.application;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.port.PlanRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Use case for retrieving plans within a date range.
 */
@Service
@RequiredArgsConstructor
public class GetPlansUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetPlansUseCase.class);
    private final PlanRepositoryPort repository;

    /**
     * Retrieves all plans overlapping [from, to], including historical ones.
     * Runs in a read-only transaction and returns an unmodifiable list.
     */
    @Transactional(Transactional.TxType.SUPPORTS)
    @Cacheable(value = "plans", key = "#from.toString() + '-' + #to.toString()")
    public List<Plan> execute(LocalDateTime from, LocalDateTime to) {
        log.info("[GetPlansUseCase] Fetching plans from {} to {}", from, to);
        Assert.notNull(from, "from must not be null");
        Assert.notNull(to,   "to   must not be null");
        Assert.isTrue(!from.isAfter(to), "from must be before or equal to to");
        try {
            List<Plan> plans = repository.findByDateRange(from, to);
            log.info("[GetPlansUseCase] Found {} plans in range.", plans.size());
            return Collections.unmodifiableList(plans);
        } catch (Exception ex) {
            log.error("[GetPlansUseCase] Error fetching plans for range {} - {}", from, to, ex);
            throw ex;
        }
    }
}
