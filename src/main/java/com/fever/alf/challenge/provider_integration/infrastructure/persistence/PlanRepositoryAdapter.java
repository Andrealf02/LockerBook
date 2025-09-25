package com.fever.alf.challenge.provider_integration.infrastructure.persistence;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.Zone;
import com.fever.alf.challenge.provider_integration.domain.port.PlanRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA adapter implementing the PlanRepositoryPort.
 * Converts between domain models and JPA entities.
 */
@Component
@RequiredArgsConstructor
public class PlanRepositoryAdapter implements PlanRepositoryPort {

    private final SpringDataPlanRepository jpa;

    /**
     * Upserts a Plan aggregate. Runs in a transaction to ensure consistency.
     */
    @Transactional
    @Override
    public void saveOrUpdate(Plan plan) {
        PlanEntity entity = toEntity(plan);
        // set back-references for bidirectional zones
        entity.getZones().forEach(z -> z.setPlan(entity));
        jpa.save(entity);
    }

    /**
     * Retrieves all plans that overlap the given date range.
     * Returns an unmodifiable list of domain Plan objects.
     */
    @Override
    public List<Plan> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return jpa.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(to, from)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    /*--------- Mapping Helpers ---------*/

    private PlanEntity toEntity(Plan plan) {
        var zoneEntities = plan.getZones().stream()
                .map(z -> ZoneEntity.builder()
                        .zoneId(z.getZoneId())
                        .name(z.getName())
                        .price(z.getPrice())
                        .capacity(z.getCapacity())
                        .numbered(z.isNumbered())
                        .build())
                .collect(Collectors.toList());

        return PlanEntity.builder()
                .id(plan.getId())
                .name(plan.getName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .sellMode(plan.getSellMode())
                .zones(zoneEntities)
                .build();
    }

    private Plan toDomain(PlanEntity e) {
        List<Zone> zones = e.getZones().stream()
                .map(this::zoneEntityToDomain)
                .collect(Collectors.toUnmodifiableList());

        return Plan.builder()
                .id(e.getId())
                .name(e.getName())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .sellMode(e.getSellMode())
                .zones(zones)
                .build();
    }

    private Zone zoneEntityToDomain(ZoneEntity ze) {
        return Zone.builder()
                .zoneId(ze.getZoneId())
                .name(ze.getName())
                .price(ze.getPrice())
                .capacity(ze.getCapacity())
                .numbered(ze.isNumbered())
                .build();
    }
}
