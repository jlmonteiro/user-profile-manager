package com.jorgemonteiro.usermanager.exception;

/**
 * Thrown when a requested resource cannot be found.
 * Maps to HTTP 404.
 */
public class ObjectNotFoundException extends UserManagerException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message human-readable description of the missing resource
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
