package com.fever.alf.challenge.provider_integration.domain.port;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import java.util.List;

public interface FetchPlansFromProviderPort {
    List<Plan> fetchAllPlans();
}
