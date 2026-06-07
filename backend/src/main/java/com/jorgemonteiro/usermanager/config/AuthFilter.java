package com.jorgemonteiro.usermanager.config;

import com.jorgemonteiro.usermanager.exception.ProblemDetail;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * JAX-RS request filter that extracts the authenticated user's email
 * from the {@code X-Auth-Request-Email} header set by oauth2-proxy.
 *
 * <p>Returns HTTP 401 if the header is missing or blank.</p>
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {

    public static final String AUTH_EMAIL_HEADER = "X-Auth-Request-Email";
    public static final String AUTH_EMAIL_PROPERTY = "auth.email";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (isHealthCheck(requestContext)) {
            return;
        }

        var email = requestContext.getHeaderString(AUTH_EMAIL_HEADER);
        if (email == null || email.isBlank()) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ProblemDetail(
                                    "https://home.monteiro.net/problems/unauthorized",
                                    "Unauthorized",
                                    401,
                                    "Missing X-Auth-Request-Email header",
                                    null))
                            .type("application/problem+json")
                            .build()
            );
            return;
        }

        requestContext.setProperty(AUTH_EMAIL_PROPERTY, email.strip());
    }

    private boolean isHealthCheck(ContainerRequestContext requestContext) {
        var path = requestContext.getUriInfo().getPath();
        return path.startsWith("/q/health");
    }
}
