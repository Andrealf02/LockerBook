package com.fever.alf.challenge.provider_integration.infrastructure.exceptions;

public class ProviderException extends RuntimeException {
    public ProviderException(String message) {
        super(message);
    }
    public ProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
