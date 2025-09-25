package com.fever.alf.challenge.provider_integration.infrastructure.persistence;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.Zone;
import com.fever.alf.challenge.provider_integration.domain.port.PlanRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({PlanRepositoryAdapter.class})
class PlanRepositoryAdapterIntegrationTest {

    @Autowired
    private PlanRepositoryPort planRepository;

    @Test
    void saveAndFindByDateRange_persistsAndFindsPlans() {
        // Arrange
        Zone zone = Zone.builder()
                .zoneId("z1")
                .capacity(100)
                .name("VIP")
                .price(new BigDecimal("50.00"))
                .numbered(true)
                .build();
        Plan plan = Plan.builder()
                .id("p1")
                .name("Test Plan")
                .startDate(LocalDateTime.parse("2021-06-30T21:00:00"))
                .endDate(LocalDateTime.parse("2021-06-30T22:00:00"))
                .sellMode("online")
                .zones(List.of(zone))
                .build();

        // Act
        planRepository.saveOrUpdate(plan);
        List<Plan> found = planRepository.findByDateRange(
                LocalDateTime.parse("2021-06-30T20:00:00"),
                LocalDateTime.parse("2021-06-30T23:00:00"));

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getZones()).hasSize(1);
        assertThat(found.get(0).getZones().get(0).getName()).isEqualTo("VIP");
    }
}
