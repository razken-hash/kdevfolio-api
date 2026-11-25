package com.api.kdevfolio.common.exceptions;

/**
 * Thrown when a requested resource does not exist. Mapped to HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String field, Object value) {
        super("%s not found with %s: %s".formatted(resource, field, value));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
