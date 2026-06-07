package com.jorgemonteiro.usermanager.exception;

/**
 * Thrown when a business rule is violated (e.g., deleting a system role, deleting the last admin).
 * Maps to HTTP 422.
 */
public class BusinessRuleException extends UserManagerException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message human-readable description of the violated rule
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
