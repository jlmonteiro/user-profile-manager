package com.jorgemonteiro.usermanager.exception;

/**
 * Thrown when a user attempts to access a resource without sufficient permissions.
 * Maps to HTTP 403.
 */
public class ForbiddenAccessException extends UserManagerException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message human-readable description of the access denial
     */
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
