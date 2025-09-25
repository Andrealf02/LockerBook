package com.fever.alf.challenge.provider_integration.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class Plan {
    private final String id;
    private final String name;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String sellMode;
    private final List<Zone> zones;

    public Plan(String id, String name, LocalDateTime startDate, LocalDateTime endDate, String sellMode, List<Zone> zones) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sellMode = sellMode;
        this.zones = zones;
    }
}
