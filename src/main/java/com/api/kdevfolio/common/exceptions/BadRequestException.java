package com.api.kdevfolio.common.exceptions;

/**
 * Thrown when a syntactically valid request is semantically invalid
 * (e.g. it references entities that do not exist). Mapped to HTTP 400.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
