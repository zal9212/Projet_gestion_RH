package com.plateforme.auth.rest;

import com.plateforme.auth.dto.Credentials;
import com.plateforme.auth.dto.UserResponse;
import com.plateforme.auth.entities.Utilisateur;
import com.plateforme.auth.services.AuthentificationService;
import com.plateforme.auth.services.UtilisateurService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private AuthentificationService authService;

    @EJB
    private UtilisateurService utilisateurService;

    @POST
    @Path("/login")
    public Response login(Credentials creds) {
        String token = authService.login(creds.getUsername(), creds.getPassword());
        
        if (token != null) {
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return Response.ok(response).build();
        }
        
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Nom d'utilisateur ou mot de passe incorrect")
                .build();
    }

    @GET
    @Path("/profile")
    public Response getProfile(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Utilisateur u = utilisateurService.findByUsername(username);
        
        if (u != null) {
            return Response.ok(new UserResponse(u)).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
