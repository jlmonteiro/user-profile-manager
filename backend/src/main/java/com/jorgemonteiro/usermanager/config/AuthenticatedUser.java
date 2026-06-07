package com.jorgemonteiro.usermanager.config;

import jakarta.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CDI qualifier that identifies the authenticated user's email.
 *
 * <p>In JAX-RS resources, use {@code @HeaderParam("X-Auth-Request-Email")} directly
 * since the filter guarantees this header is present for all non-health endpoints.</p>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface AuthenticatedUser {
}
