package com.plateforme.absences.rest;

import com.plateforme.absences.dto.ApiResponse;
import com.plateforme.absences.dto.TypeAbsenceDTO;
import com.plateforme.absences.entities.TypeAbsence;
import com.plateforme.absences.services.TypeAbsenceService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * API REST pour gérer les types d'absences.
 * 
 * Endpoints :
 * - GET    /api/absences/types         → Liste tous les types
 * - GET    /api/absences/types/{id}    → Détails d'un type
 * - POST   /api/absences/types         → Créer un type (ADMIN)
 * - PUT    /api/absences/types/{id}    → Modifier un type (ADMIN)
 * - DELETE /api/absences/types/{id}    → Supprimer un type (ADMIN)
 */
@Path("/absences/types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TypeAbsenceResource {
    
    private static final Logger LOGGER = Logger.getLogger(TypeAbsenceResource.class.getName());
    
    @EJB
    private TypeAbsenceService typeAbsenceService;
    
    /**
     * GET /api/absences/types
     * Liste tous les types d'absences.
     */
    @GET
    public Response findAll() {
        LOGGER.info("GET /api/absences/types");
        
        try {
            List<TypeAbsence> types = typeAbsenceService.findAll();
            List<TypeAbsenceDTO> dtos = types.stream()
                .map(TypeAbsenceDTO::new)
                .collect(Collectors.toList());
            
            return Response.ok(ApiResponse.success("Types récupérés", dtos)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/absences/types/{id}
     * Récupère un type d'absence par son ID.
     */
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        LOGGER.info("GET /api/absences/types/" + id);
        
        try {
            TypeAbsence type = typeAbsenceService.findById(id);
            
            if (type == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Type non trouvé"))
                    .build();
            }
            
            return Response.ok(ApiResponse.success("Type trouvé", new TypeAbsenceDTO(type))).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * POST /api/absences/types
     * Crée un nouveau type d'absence.
     */
    @POST
    public Response create(TypeAbsenceDTO dto) {
        LOGGER.info("POST /api/absences/types");
        
        try {
            // Validation
            if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le nom est obligatoire"))
                    .build();
            }
            
            // Créer l'entité
            TypeAbsence type = new TypeAbsence();
            type.setNom(dto.getNom());
            type.setDescription(dto.getDescription());
            type.setJustificationRequise(dto.getJustificationRequise());
            
            // Sauvegarder
            type = typeAbsenceService.create(type);
            
            return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Type créé", new TypeAbsenceDTO(type)))
                .build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * PUT /api/absences/types/{id}
     * Modifie un type d'absence.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, TypeAbsenceDTO dto) {
        LOGGER.info("PUT /api/absences/types/" + id);
        
        try {
            TypeAbsence type = typeAbsenceService.findById(id);
            
            if (type == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Type non trouvé"))
                    .build();
            }
            
            // Mettre à jour
            type.setNom(dto.getNom());
            type.setDescription(dto.getDescription());
            type.setJustificationRequise(dto.getJustificationRequise());
            
            type = typeAbsenceService.update(type);
            
            return Response.ok(ApiResponse.success("Type modifié", new TypeAbsenceDTO(type))).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * DELETE /api/absences/types/{id}
     * Supprime un type d'absence.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        LOGGER.info("DELETE /api/absences/types/" + id);
        
        try {
            TypeAbsence type = typeAbsenceService.findById(id);
            
            if (type == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Type non trouvé"))
                    .build();
            }
            
            typeAbsenceService.delete(id);
            
            return Response.ok(ApiResponse.success("Type supprimé")).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
}
