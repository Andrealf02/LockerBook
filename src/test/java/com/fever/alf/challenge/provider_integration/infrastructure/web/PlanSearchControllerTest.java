// java
package com.fever.alf.challenge.provider_integration.infrastructure.web;

import com.fever.alf.challenge.provider_integration.application.GetPlansUseCase;
import com.fever.alf.challenge.provider_integration.domain.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanSearchController.class)
class PlanSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GetPlansUseCase getPlansUseCase;

    private Plan plan;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public GetPlansUseCase getPlansUseCase() {
            return Mockito.mock(GetPlansUseCase.class);
        }
    }

    @BeforeEach
    void setUp() {
        plan = Plan.builder()
                .id("291")
                .name("Camela en concierto")
                .startDate(LocalDateTime.parse("2021-06-30T21:00:00"))
                .endDate(LocalDateTime.parse("2021-06-30T22:00:00"))
                .sellMode("online")
                .zones(Collections.emptyList())
                .build();
    }

    @Test
    void getPlans_returnsOnlinePlansInRange() throws Exception {
        Mockito.when(getPlansUseCase.execute(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(plan));

        mockMvc.perform(get("/api/plans")
                        .param("starts_at", "2021-06-30T21:00:00")
                        .param("ends_at", "2021-06-30T22:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("291"))
                .andExpect(jsonPath("$[0].name").value("Camela en concierto"));
    }

    @Test
    void getPlans_returnsEmptyListWhenNoPlans() throws Exception {
        Mockito.when(getPlansUseCase.execute(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/plans")
                        .param("starts_at", "2021-07-31T20:00:00")
                        .param("ends_at", "2021-07-31T21:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
    @Test
    void getPlans_returnsBadRequestOnInvalidDateFormat() throws Exception {
        mockMvc.perform(get("/api/plans")
                        .param("starts_at", "invalid-date")
                        .param("ends_at", "2021-06-30T22:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}