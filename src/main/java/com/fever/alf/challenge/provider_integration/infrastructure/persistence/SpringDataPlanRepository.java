package com.fever.alf.challenge.provider_integration.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataPlanRepository
        extends JpaRepository<PlanEntity, String> {
    List<PlanEntity> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDateTime to, LocalDateTime from);
}
