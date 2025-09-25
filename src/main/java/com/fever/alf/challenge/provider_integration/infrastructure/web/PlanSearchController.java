package com.fever.alf.challenge.provider_integration.infrastructure.web;

import com.fever.alf.challenge.provider_integration.application.GetPlansUseCase;
import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.infrastructure.web.dto.PlanDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class PlanSearchController {

    private final GetPlansUseCase getPlansUseCase;
    private static final Logger log = LoggerFactory.getLogger(PlanSearchController.class);

    public PlanSearchController(GetPlansUseCase getPlansUseCase) {
        this.getPlansUseCase = getPlansUseCase;
    }

    @Operation(
        summary = "Get available plans in a date range",
        description = "Returns all plans with sell_mode=online that were ever available in the given date range.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of plans",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanDto.class))
            )
        }
    )
    @GetMapping("/plans")
    public ResponseEntity<List<PlanDto>> getPlans(
            @Parameter(description = "Start of the date range (inclusive)", example = "2021-06-30T21:00:00")
            @RequestParam("starts_at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startsAt,
            @Parameter(description = "End of the date range (inclusive)", example = "2021-06-30T22:00:00")
            @RequestParam("ends_at")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endsAt) {
        log.info("[PlanSearchController] GET /api/plans called with starts_at={} ends_at={}", startsAt, endsAt);
        List<Plan> plans = getPlansUseCase.execute(startsAt, endsAt);
        List<PlanDto> onlinePlans = plans.stream()
                .filter(plan -> "online".equalsIgnoreCase(plan.getSellMode()))
                .map(PlanDto::fromDomain)
                .collect(Collectors.toList());
        log.info("[PlanSearchController] Returning {} online plans.", onlinePlans.size());
        return ResponseEntity.ok(onlinePlans);
    }
}
