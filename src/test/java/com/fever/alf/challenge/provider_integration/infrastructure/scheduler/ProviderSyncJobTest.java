package com.fever.alf.challenge.provider_integration.infrastructure.scheduler;

import com.fever.alf.challenge.provider_integration.application.SyncPlansUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class ProviderSyncJobTest {
    private SyncPlansUseCase syncPlansUseCase;
    private ProviderSyncJob providerSyncJob;

    @BeforeEach
    void setUp() {
        syncPlansUseCase = mock(SyncPlansUseCase.class);
        providerSyncJob = new ProviderSyncJob(syncPlansUseCase);
    }

    @Test
    void sync_executesSyncPlansUseCaseSuccessfully() {
        providerSyncJob.sync();
        verify(syncPlansUseCase, times(1)).execute();
    }

    @Test
    void sync_handlesExceptionGracefully() {
        doThrow(new RuntimeException("sync failed")).when(syncPlansUseCase).execute();
        providerSyncJob.sync();
        verify(syncPlansUseCase, times(1)).execute();
    }
}

