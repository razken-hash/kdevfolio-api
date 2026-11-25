package com.api.kdevfolio.common.exceptions;

/**
 * Thrown when a request conflicts with the current state of a resource
 * (duplicate unique value, resource still in use, ...). Mapped to HTTP 409.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
