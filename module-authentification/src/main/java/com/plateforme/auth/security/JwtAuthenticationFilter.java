package com.plateforme.auth.security;

import com.plateforme.auth.services.SecurityService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @EJB
    private SecurityService securityService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Obtenir le header Authorization
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Si pas de header ou si ce n'est pas un Bearer token, on laisse passer (les ressources @RolesAllowed bloqueront plus tard)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        // Extraire le token
        String token = authHeader.substring(7);

        try {
            // Valider le token
            Claims claims = securityService.validateToken(token);

            if (claims != null) {
                final String username = claims.getSubject();
                final String role = claims.get("role", String.class);

                // Créer un SecurityContext personnalisé
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return () -> username;
                    }

                    @Override
                    public boolean isUserInRole(String r) {
                        return role.equalsIgnoreCase(r);
                    }

                    @Override
                    public boolean isSecure() {
                        return requestContext.getSecurityContext().isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "Bearer";
                    }
                });
            } else {
                // Token invalide
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Token invalide ou expiré").build());
            }
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
