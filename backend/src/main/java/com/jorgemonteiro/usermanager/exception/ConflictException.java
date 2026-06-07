package com.jorgemonteiro.usermanager.exception;

/**
 * Thrown when an operation conflicts with existing state (e.g., duplicate name).
 * Maps to HTTP 409.
 */
public class ConflictException extends UserManagerException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message human-readable description of the conflict
     */
    public ConflictException(String message) {
        super(message);
    }
}
