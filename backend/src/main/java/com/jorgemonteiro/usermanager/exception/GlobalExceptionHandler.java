package com.jorgemonteiro.usermanager.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

/**
 * Central exception handler that converts application exceptions into RFC 7807 responses.
 */
public class GlobalExceptionHandler {

    private static final String PROBLEM_JSON = "application/problem+json";
    private static final String BASE_URI = "https://home.monteiro.net/problems/";

    /**
     * Handles forbidden access attempts.
     */
    @ServerExceptionMapper
    public Response handleForbidden(ForbiddenAccessException e) {
        return problemResponse(403, AppErrorType.FORBIDDEN, e.getMessage());
    }

    /**
     * Handles resource not found.
     */
    @ServerExceptionMapper
    public Response handleNotFound(ObjectNotFoundException e) {
        return problemResponse(404, AppErrorType.NOT_FOUND, e.getMessage());
    }

    /**
     * Handles conflict (duplicates).
     */
    @ServerExceptionMapper
    public Response handleConflict(ConflictException e) {
        return problemResponse(409, AppErrorType.CONFLICT, e.getMessage());
    }

    /**
     * Handles business rule violations.
     */
    @ServerExceptionMapper
    public Response handleBusinessRule(BusinessRuleException e) {
        return problemResponse(422, AppErrorType.BUSINESS_RULE_VIOLATION, e.getMessage());
    }

    /**
     * Handles bean validation errors.
     */
    @ServerExceptionMapper
    public Response handleValidation(ConstraintViolationException e) {
        var detail = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");

        return problemResponse(400, AppErrorType.VALIDATION_ERROR, detail);
    }

    private Response problemResponse(int status, AppErrorType errorType, String detail) {
        var type = BASE_URI + errorType.name().toLowerCase().replace("_", "-");
        var title = errorType.name().replace("_", " ");
        title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();

        var problem = new ProblemDetail(type, title, status, detail, null);
        return Response.status(status)
                .entity(problem)
                .type(PROBLEM_JSON)
                .build();
    }
}
