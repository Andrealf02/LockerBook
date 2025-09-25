package com.fever.alf.challenge.provider_integration.domain.port;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import java.time.LocalDateTime;
import java.util.List;

public interface PlanRepositoryPort {
    void saveOrUpdate(Plan plan);
    List<Plan> findByDateRange(LocalDateTime from, LocalDateTime to);
}
