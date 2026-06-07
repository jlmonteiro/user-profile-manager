package com.jorgemonteiro.usermanager.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * RFC 7807 Problem Detail response body.
 *
 * @param type     URI identifying the error category
 * @param title    short human-readable summary
 * @param status   HTTP status code
 * @param detail   human-readable explanation
 * @param instance the request path that caused the error
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetail(
        String type,
        String title,
        int status,
        String detail,
        String instance
) {
}
