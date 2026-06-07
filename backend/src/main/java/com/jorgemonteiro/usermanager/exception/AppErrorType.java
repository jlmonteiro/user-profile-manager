package com.jorgemonteiro.usermanager.exception;

/**
 * Standardized error types for the application.
 * Mapped to the 'type' field in RFC 7807 ProblemDetail responses.
 */
public enum AppErrorType {
    /** Authentication failure (401). */
    UNAUTHORIZED,

    /** Authorization failure (403). */
    FORBIDDEN,

    /** Resource not found (404). */
    NOT_FOUND,

    /** Conflict with existing state (409). */
    CONFLICT,

    /** Input validation failed (400). */
    VALIDATION_ERROR,

    /** Business rule violation (422). */
    BUSINESS_RULE_VIOLATION,

    /** Unexpected internal server error (500). */
    INTERNAL_SERVER_ERROR
}
