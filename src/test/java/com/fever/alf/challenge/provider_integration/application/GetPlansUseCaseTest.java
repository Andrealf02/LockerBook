package com.fever.alf.challenge.provider_integration.application;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.port.PlanRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetPlansUseCaseTest {
    private PlanRepositoryPort repository;
    private GetPlansUseCase getPlansUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(PlanRepositoryPort.class);
        getPlansUseCase = new GetPlansUseCase(repository);
    }

    @Test
    void execute_returnsPlansInRange() {
        Plan plan = Plan.builder()
                .id("1")
                .name("Test Plan")
                .startDate(LocalDateTime.parse("2021-06-30T21:00:00"))
                .endDate(LocalDateTime.parse("2021-06-30T22:00:00"))
                .sellMode("online")
                .zones(Collections.emptyList())
                .build();
        when(repository.findByDateRange(any(), any())).thenReturn(List.of(plan));

        List<Plan> result = getPlansUseCase.execute(
                LocalDateTime.parse("2021-06-30T20:00:00"),
                LocalDateTime.parse("2021-06-30T23:00:00"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Plan");
    }

    @Test
    void execute_returnsEmptyListWhenNoPlans() {
        when(repository.findByDateRange(any(), any())).thenReturn(Collections.emptyList());
        List<Plan> result = getPlansUseCase.execute(
                LocalDateTime.parse("2021-06-30T20:00:00"),
                LocalDateTime.parse("2021-06-30T23:00:00"));
        assertThat(result).isEmpty();
    }

    @Test
    void execute_throwsExceptionWhenFromIsAfterTo() {
        assertThatThrownBy(() -> getPlansUseCase.execute(
                LocalDateTime.parse("2021-06-30T23:00:00"),
                LocalDateTime.parse("2021-06-30T20:00:00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("from must be before or equal to to");
    }

    @Test
    void execute_throwsExceptionWhenFromIsNull() {
        assertThatThrownBy(() -> getPlansUseCase.execute(
                null,
                LocalDateTime.parse("2021-06-30T20:00:00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("from must not be null");
    }

    @Test
    void execute_throwsExceptionWhenToIsNull() {
        assertThatThrownBy(() -> getPlansUseCase.execute(
                LocalDateTime.parse("2021-06-30T20:00:00"),
                null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("to   must not be null");
    }
}

