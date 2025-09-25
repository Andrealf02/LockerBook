package com.fever.alf.challenge.provider_integration.infrastructure.scheduler;

import com.fever.alf.challenge.provider_integration.application.SyncPlansUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.ApplicationArguments;

import static org.mockito.Mockito.*;

class ProviderSyncOnStartupTest {
    private SyncPlansUseCase syncPlansUseCase;
    private ProviderSyncOnStartup providerSyncOnStartup;
    private ApplicationArguments args;

    @BeforeEach
    void setUp() {
        syncPlansUseCase = mock(SyncPlansUseCase.class);
        providerSyncOnStartup = new ProviderSyncOnStartup(syncPlansUseCase);
        args = mock(ApplicationArguments.class);
    }

    @Test
    void run_executesSyncPlansUseCaseSuccessfully() {
        providerSyncOnStartup.run(args);
        verify(syncPlansUseCase, times(1)).execute();
    }

    @Test
    void run_handlesExceptionGracefully() {
        doThrow(new RuntimeException("sync failed")).when(syncPlansUseCase).execute();
        providerSyncOnStartup.run(args);
        verify(syncPlansUseCase, times(1)).execute();
    }
}

