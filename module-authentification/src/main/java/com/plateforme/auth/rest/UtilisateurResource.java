package com.plateforme.auth.rest;

import com.plateforme.auth.dto.UserResponse;
import com.plateforme.auth.entities.Utilisateur;
import com.plateforme.auth.services.UtilisateurService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurResource {

    @EJB
    private UtilisateurService utilisateurService;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAll() {
        List<UserResponse> users = utilisateurService.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return Response.ok(users).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response create(Utilisateur u) {
        Utilisateur created = utilisateurService.create(u);
        return Response.status(Response.Status.CREATED).entity(new UserResponse(created)).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Utilisateur u = utilisateurService.findById(id);
        if (u != null) {
            return Response.ok(new UserResponse(u)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Utilisateur u) {
        Utilisateur existing = utilisateurService.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        u.setId(id);
        // On ne change pas le mot de passe via cet endpoint
        u.setMotDePasse(existing.getMotDePasse());
        Utilisateur updated = utilisateurService.update(u);
        return Response.ok(new UserResponse(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        utilisateurService.delete(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/password")
    public Response changePassword(@PathParam("id") Long id, String newPassword) {
        if (utilisateurService.changePassword(id, newPassword)) {
            return Response.ok("Mot de passe mis à jour").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
