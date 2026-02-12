package com.plateforme.absences.rest;

import com.plateforme.absences.dto.AbsenceDTO;
import com.plateforme.absences.dto.ApiResponse;
import com.plateforme.absences.entities.Absence;
import com.plateforme.absences.entities.TypeAbsence;
import com.plateforme.absences.services.AbsenceService;
import com.plateforme.absences.services.TypeAbsenceService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * API REST pour gérer les absences.
 * 
 * Endpoints :
 * - GET    /api/absences                    → Liste toutes les absences
 * - GET    /api/absences/{id}               → Détails d'une absence
 * - GET    /api/absences/employe/{id}       → Absences d'un employé
 * - GET    /api/absences/periode            → Absences sur une période
 * - GET    /api/absences/non-justifiees     → Absences non justifiées
 * - POST   /api/absences                    → Créer une absence
 * - PUT    /api/absences/{id}               → Modifier une absence
 * - PUT    /api/absences/{id}/justifier     → Justifier une absence
 * - DELETE /api/absences/{id}               → Supprimer une absence
 */
@Path("/absences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AbsenceResource {
    
    private static final Logger LOGGER = Logger.getLogger(AbsenceResource.class.getName());
    
    @Context
    private SecurityContext securityContext;

    @EJB
    private AbsenceService absenceService;
    
    @EJB
    private TypeAbsenceService typeAbsenceService;
    
    /**
     * GET /api/absences
     * Liste toutes les absences.
     */
    @GET
    public Response findAll() {
        LOGGER.info("GET /api/absences");

        if (!securityContext.isUserInRole("MANAGER") && !securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity(ApiResponse.error("Accès refusé : Droits Manager/Admin requis"))
                           .build();
        }
        
        try {
            List<Absence> absences = absenceService.findAll();
            List<AbsenceDTO> dtos = absences.stream()
                .map(AbsenceDTO::new)
                .collect(Collectors.toList());
            
            return Response.ok(ApiResponse.success("Absences récupérées", dtos)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/absences/{id}
     * Récupère une absence par son ID.
     */
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        LOGGER.info("GET /api/absences/" + id);
        
        try {
            Absence absence = absenceService.findById(id);
            
            if (absence == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Absence non trouvée"))
                    .build();
            }
            
            return Response.ok(ApiResponse.success("Absence trouvée", new AbsenceDTO(absence))).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/absences/employe/{employeId}
     * Récupère les absences d'un employé.
     */
    @GET
    @Path("/employe/{employeId}")
    public Response findByEmploye(@PathParam("employeId") Long employeId) {
        LOGGER.info("GET /api/absences/employe/" + employeId);
        
        try {
            List<Absence> absences = absenceService.findByEmploye(employeId);
            List<AbsenceDTO> dtos = absences.stream()
                .map(AbsenceDTO::new)
                .collect(Collectors.toList());
            
            return Response.ok(ApiResponse.success("Absences récupérées", dtos)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/absences/periode?debut=2026-01-01&fin=2026-12-31
     * Récupère les absences sur une période.
     */
    @GET
    @Path("/periode")
    public Response findByPeriode(
            @QueryParam("debut") String debutStr,
            @QueryParam("fin") String finStr) {
        
        LOGGER.info("GET /api/absences/periode?debut=" + debutStr + "&fin=" + finStr);

        if (!securityContext.isUserInRole("MANAGER") && !securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity(ApiResponse.error("Accès refusé : Droits Manager/Admin requis"))
                           .build();
        }
        
        try {
            // Validation des paramètres
            if (debutStr == null || finStr == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Les paramètres debut et fin sont obligatoires"))
                    .build();
            }
            
            // Conversion des dates
            LocalDate debut = LocalDate.parse(debutStr);
            LocalDate fin = LocalDate.parse(finStr);
            
            List<Absence> absences = absenceService.findByPeriode(debut, fin);
            List<AbsenceDTO> dtos = absences.stream()
                .map(AbsenceDTO::new)
                .collect(Collectors.toList());
            
            return Response.ok(ApiResponse.success("Absences récupérées", dtos)).build();
            
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.error("Format de date invalide (utiliser YYYY-MM-DD)"))
                .build();
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/absences/non-justifiees
     * Récupère les absences non justifiées.
     */
    @GET
    @Path("/non-justifiees")
    public Response findNonJustifiees() {
        LOGGER.info("GET /api/absences/non-justifiees");

        if (!securityContext.isUserInRole("MANAGER") && !securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity(ApiResponse.error("Accès refusé : Droits Manager/Admin requis"))
                           .build();
        }
        
        try {
            List<Absence> absences = absenceService.findNonJustifiees();
            List<AbsenceDTO> dtos = absences.stream()
                .map(AbsenceDTO::new)
                .collect(Collectors.toList());
            
            return Response.ok(ApiResponse.success("Absences non justifiées", dtos)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * POST /api/absences
     * Crée une nouvelle absence.
     * 
     * Body JSON :
     * {
     *   "employeId": 5,
     *   "typeAbsenceId": 1,
     *   "dateAbsence": "2026-02-10",
     *   "heureDebut": "08:00:00",
     *   "heureFin": "17:00:00",
     *   "dureeHeures": 8.0,
     *   "motif": "Grippe",
     *   "enregistrePar": 2
     * }
     */
    @POST
    public Response create(AbsenceDTO dto) {
        LOGGER.info("POST /api/absences");
        
        try {
            // Validation
            if (dto.getEmployeId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("L'ID de l'employé est obligatoire"))
                    .build();
            }
            
            if (dto.getTypeAbsenceId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le type d'absence est obligatoire"))
                    .build();
            }
            
            if (dto.getDateAbsence() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("La date d'absence est obligatoire"))
                    .build();
            }
            
            // Récupérer le type d'absence
            TypeAbsence typeAbsence = typeAbsenceService.findById(dto.getTypeAbsenceId());
            if (typeAbsence == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Type d'absence invalide"))
                    .build();
            }
            
            // Créer l'entité Absence
            Absence absence = new Absence();
            absence.setEmployeId(dto.getEmployeId());
            absence.setTypeAbsence(typeAbsence);
            absence.setDateAbsence(dto.getDateAbsence());
            absence.setHeureDebut(dto.getHeureDebut());
            absence.setHeureFin(dto.getHeureFin());
            absence.setDureeHeures(dto.getDureeHeures() != null ? dto.getDureeHeures() : BigDecimal.valueOf(8.0));
            absence.setMotif(dto.getMotif());
            absence.setJustifiee(dto.getJustifiee() != null ? dto.getJustifiee() : false);
            absence.setStatut(dto.getStatut() != null ? dto.getStatut() : "EN_ATTENTE");
            absence.setEnregistrePar(dto.getEnregistrePar());
            
            // Sauvegarder (envoie aussi une notification JMS)
            absence = absenceService.create(absence);
            
            return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Absence créée avec succès", new AbsenceDTO(absence)))
                .build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur : " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * PUT /api/absences/{id}
     * Modifie une absence.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, AbsenceDTO dto) {
        LOGGER.info("PUT /api/absences/" + id);
        
        try {
            Absence absence = absenceService.findById(id);
            
            if (absence == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Absence non trouvée"))
                    .build();
            }
            
            // Mettre à jour les champs modifiables
            if (dto.getTypeAbsenceId() != null) {
                TypeAbsence typeAbsence = typeAbsenceService.findById(dto.getTypeAbsenceId());
                if (typeAbsence != null) {
                    absence.setTypeAbsence(typeAbsence);
                }
            }
            
            if (dto.getDateAbsence() != null) {
                absence.setDateAbsence(dto.getDateAbsence());
            }
            
            if (dto.getHeureDebut() != null) {
                absence.setHeureDebut(dto.getHeureDebut());
            }
            
            if (dto.getHeureFin() != null) {
                absence.setHeureFin(dto.getHeureFin());
            }
            
            if (dto.getDureeHeures() != null) {
                absence.setDureeHeures(dto.getDureeHeures());
            }
            
            if (dto.getMotif() != null) {
                absence.setMotif(dto.getMotif());
            }
            
            if (dto.getJustifiee() != null) {
                absence.setJustifiee(dto.getJustifiee());
            }
            
            // Sauvegarder
            absence = absenceService.update(absence);
            
            return Response.ok(ApiResponse.success("Absence modifiée", new AbsenceDTO(absence))).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * PUT /api/absences/{id}/justifier
     * Justifie une absence en ajoutant un document.
     * 
     * Body JSON :
     * {
     *   "documentPath": "/uploads/certificat_medical_123.pdf"
     * }
     */
    @PUT
    @Path("/{id}/justifier")
    public Response justifier(@PathParam("id") Long id, JustificationRequest request) {
        LOGGER.info("PUT /api/absences/" + id + "/justifier");
        
        try {
            if (request.getDocumentPath() == null || request.getDocumentPath().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le chemin du document est obligatoire"))
                    .build();
            }
            
            Absence absence = absenceService.justifier(id, request.getDocumentPath());
            
            if (absence == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Absence non trouvée"))
                    .build();
            }
            
            return Response.ok(ApiResponse.success("Absence justifiée", new AbsenceDTO(absence))).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * DELETE /api/absences/{id}
     * Supprime une absence.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        LOGGER.info("DELETE /api/absences/" + id);
        
        try {
            Absence absence = absenceService.findById(id);
            
            if (absence == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Absence non trouvée"))
                    .build();
            }
            
            absenceService.delete(id);
            
            return Response.ok(ApiResponse.success("Absence supprimée")).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * Classe interne pour la requête de justification.
     */
    public static class JustificationRequest {
        private String documentPath;
        
        public JustificationRequest() {
        }
        
        public String getDocumentPath() {
            return documentPath;
        }
        
        public void setDocumentPath(String documentPath) {
            this.documentPath = documentPath;
        }
    }
}
