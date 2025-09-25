package com.fever.alf.challenge.provider_integration.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanEntity {
    @Id
    private String id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String sellMode;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "plan"
    )
    @Singular("zone")
    private List<ZoneEntity> zones;
}
