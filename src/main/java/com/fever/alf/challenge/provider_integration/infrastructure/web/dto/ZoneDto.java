package com.fever.alf.challenge.provider_integration.infrastructure.web.dto;

import com.fever.alf.challenge.provider_integration.domain.Zone;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneDto {
    private String zoneId;
    private int capacity;
    private String name;
    private BigDecimal price;
    private boolean numbered;

    public static ZoneDto fromDomain(Zone z) {
        return ZoneDto.builder()
                .zoneId(z.getZoneId())
                .capacity(z.getCapacity())
                .name(z.getName())
                .price(z.getPrice())
                .numbered(z.isNumbered())
                .build();
    }
}
