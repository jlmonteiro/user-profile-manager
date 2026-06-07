package com.jorgemonteiro.usermanager.exception;

/**
 * Base class for all application-specific errors.
 * All custom exceptions must extend this class.
 * Direct instantiation is discouraged; create specific subclasses for each error condition.
 */
public abstract class UserManagerException extends RuntimeException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message human-readable description of the error
     */
    protected UserManagerException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given message and root cause.
     *
     * @param message human-readable description of the error
     * @param cause   the underlying exception that triggered this error
     */
    protected UserManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
