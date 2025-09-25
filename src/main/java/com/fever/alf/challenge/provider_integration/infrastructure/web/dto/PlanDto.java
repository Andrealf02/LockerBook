package com.fever.alf.challenge.provider_integration.infrastructure.web.dto;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO representing a Plan for API responses.
 */
@Schema(description = "Plan returned by the API")
public record PlanDto(
        @Schema(description = "Unique plan identifier", example = "plan_123") String id,
        @Schema(description = "Human-readable plan name", example = "Early Bird") String name,
        @Schema(description = "Plan start timestamp", example = "2025-07-01T00:00:00") LocalDateTime startDate,
        @Schema(description = "Plan end   timestamp", example = "2025-07-31T23:59:59") LocalDateTime endDate,
        @Schema(description = "List of pricing zones") List<ZoneDto> zones
) {
    public static PlanDto fromDomain(Plan p) {
        var unmZones = p.getZones().stream()
                .map(ZoneDto::fromDomain)
                .collect(Collectors.toUnmodifiableList());

        return new PlanDto(
                p.getId(), p.getName(), p.getStartDate(), p.getEndDate(), unmZones
        );
    }
}
