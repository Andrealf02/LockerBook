// File: src/main/java/com/fever/alf/challenge/provider_integration/infrastructure/web/dto/PlanQueryDto.java
package com.fever.alf.challenge.provider_integration.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record PlanQueryDto(
        @NotNull(message = "starts_at is required")
        @Parameter(description = "Start date in ISO-8601 (yyyy-MM-dd)", example = "2025-07-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startsAt,

        @NotNull(message = "ends_at is required")
        @Parameter(description = "End date in ISO-8601 (yyyy-MM-dd)", example = "2025-07-31")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endsAt
) {
    @AssertTrue(message = "starts_at must be before or equal to ends_at")
    public boolean isValidRange() {
        if (startsAt == null || endsAt == null) {
            return true;
        }
        return !startsAt.isAfter(endsAt);
    }
}