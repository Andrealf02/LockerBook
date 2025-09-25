package com.fever.alf.challenge.provider_integration.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Zone {
    private final String zoneId;
    private final int capacity;
    private final String name;
    private final BigDecimal price;
    private final boolean numbered;

    public Zone(String zoneId, int capacity, String name, BigDecimal price, boolean numbered) {
        this.zoneId = zoneId;
        this.capacity = capacity;
        this.name = name;
        this.price = price;
        this.numbered = numbered;
    }
}
