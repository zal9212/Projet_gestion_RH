package com.plateforme.absences.rest;

import com.plateforme.absences.dto.ApiResponse;
import com.plateforme.absences.dto.NotificationDTO;
import com.plateforme.absences.entities.Notification;
import com.plateforme.absences.services.NotificationService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * API REST pour gérer les notifications.
 * 
 * Endpoints :
 * - GET    /api/notifications                    → Mes notifications
 * - GET    /api/notifications/non-lues          → Mes notifications non lues
 * - GET    /api/notifications/count-non-lues    → Nombre de notifications non lues
 * - GET    /api/notifications/{id}              → Détails d'une notification
 * - PUT    /api/notifications/{id}/lire         → Marquer comme lue
 * - PUT    /api/notifications/tout-lire         → Tout marquer comme lu
 * - DELETE /api/notifications/{id}              → Supprimer une notification
 * - POST   /api/notifications                   → Créer une notification (système)
 */
@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationResource.class.getName());
    
    @EJB
    private NotificationService notificationService;
    
    /**
     * GET /api/notifications?userId={userId}
     * Récupère toutes les notifications d'un utilisateur.
     */
    @GET
    public Response findByDestinataire(@QueryParam("userId") Long userId) {
        LOGGER.info("GET /api/notifications?userId=" + userId);
        
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le paramètre userId est obligatoire"))
                    .build();
            }
            
            List<Notification> notifications = notificationService.findByDestinataire(userId);
            List<NotificationDTO> dtos = notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
            
            return Response.ok(ApiResponse.success("Notifications récupérées", dtos)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/notifications/non-lues?userId={userId}
     * Récupère les notifications non lues d'un utilisateur.
     */
    @GET
    @Path("/non-lues")
    public Response findNonLues(@QueryParam("userId") Long userId) {
        LOGGER.info("GET /api/notifications/non-lues?userId=" + userId);
        
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le paramètre userId est obligatoire"))
                    .build();
            }
            
            List<Notification> notifications = notificationService.findNonLues(userId);
            List<NotificationDTO> dtos = notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
            
            return Response.ok(ApiResponse.success("Notifications non lues", dtos)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/notifications/count-non-lues?userId={userId}
     * Compte les notifications non lues.
     */
    @GET
    @Path("/count-non-lues")
    public Response countNonLues(@QueryParam("userId") Long userId) {
        LOGGER.info("GET /api/notifications/count-non-lues?userId=" + userId);
        
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le paramètre userId est obligatoire"))
                    .build();
            }
            
            Long count = notificationService.countNonLues(userId);
            
            return Response.ok(ApiResponse.success("Nombre de notifications non lues", count)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/notifications/{id}
     * Récupère une notification par son ID.
     */
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        LOGGER.info("GET /api/notifications/" + id);
        
        try {
            Notification notification = notificationService.findById(id);
            
            if (notification == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Notification non trouvée"))
                    .build();
            }
            
            return Response.ok(ApiResponse.success("Notification trouvée", new NotificationDTO(notification))).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * POST /api/notifications
     * Crée une nouvelle notification.
     * 
     * Body JSON :
     * {
     *   "destinataireId": 5,
     *   "expediteurId": 2,
     *   "type": "INFO",
     *   "sujet": "Notification de test",
     *   "message": "Ceci est un message de test"
     * }
     */
    @POST
    public Response create(NotificationDTO dto) {
        LOGGER.info("POST /api/notifications");
        
        try {
            // Validation
            if (dto.getDestinataireId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le destinataire est obligatoire"))
                    .build();
            }
            
            if (dto.getType() == null || dto.getType().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le type est obligatoire"))
                    .build();
            }
            
            if (dto.getSujet() == null || dto.getSujet().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le sujet est obligatoire"))
                    .build();
            }
            
            if (dto.getMessage() == null || dto.getMessage().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le message est obligatoire"))
                    .build();
            }
            
            // Créer la notification
            Notification notification = notificationService.creerNotification(
                dto.getDestinataireId(),
                dto.getExpediteurId(),
                dto.getType(),
                dto.getSujet(),
                dto.getMessage()
            );
            
            if (dto.getReferenceId() != null) {
                notification.setReferenceId(dto.getReferenceId());
            }
            
            if (dto.getTypeReference() != null) {
                notification.setTypeReference(dto.getTypeReference());
            }
            
            return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Notification créée", new NotificationDTO(notification)))
                .build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * PUT /api/notifications/{id}/lire
     * Marque une notification comme lue.
     */
    @PUT
    @Path("/{id}/lire")
    public Response marquerCommeLue(@PathParam("id") Long id) {
        LOGGER.info("PUT /api/notifications/" + id + "/lire");
        
        try {
            Notification notification = notificationService.marquerCommeLue(id);
            
            if (notification == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Notification non trouvée"))
                    .build();
            }
            
            return Response.ok(ApiResponse.success("Notification marquée comme lue", new NotificationDTO(notification))).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * PUT /api/notifications/tout-lire?userId={userId}
     * Marque toutes les notifications d'un utilisateur comme lues.
     */
    @PUT
    @Path("/tout-lire")
    public Response marquerToutesCommeLues(@QueryParam("userId") Long userId) {
        LOGGER.info("PUT /api/notifications/tout-lire?userId=" + userId);
        
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le paramètre userId est obligatoire"))
                    .build();
            }
            
            int count = notificationService.marquerToutesCommeLues(userId);
            
            return Response.ok(ApiResponse.success(count + " notification(s) marquée(s) comme lue(s)", count)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * DELETE /api/notifications/{id}
     * Supprime une notification.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        LOGGER.info("DELETE /api/notifications/" + id);
        
        try {
            boolean deleted = notificationService.delete(id);
            
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Notification non trouvée"))
                    .build();
            }
            
            return Response.ok(ApiResponse.success("Notification supprimée")).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * GET /api/notifications/statistiques?userId={userId}
     * Récupère les statistiques de notifications.
     */
    @GET
    @Path("/statistiques")
    public Response getStatistiques(@QueryParam("userId") Long userId) {
        LOGGER.info("GET /api/notifications/statistiques?userId=" + userId);
        
        try {
            if (userId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Le paramètre userId est obligatoire"))
                    .build();
            }
            
            Long[] stats = notificationService.getStatistiques(userId);
            
            StatistiquesResponse response = new StatistiquesResponse();
            response.setTotal(stats[0]);
            response.setNonLues(stats[1]);
            response.setLues(stats[2]);
            
            return Response.ok(ApiResponse.success("Statistiques", response)).build();
            
        } catch (Exception e) {
            LOGGER.severe("Erreur : " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Erreur serveur"))
                .build();
        }
    }
    
    /**
     * Classe interne pour les statistiques.
     */
    public static class StatistiquesResponse {
        private Long total;
        private Long nonLues;
        private Long lues;
        
        public StatistiquesResponse() {
        }
        
        public Long getTotal() {
            return total;
        }
        
        public void setTotal(Long total) {
            this.total = total;
        }
        
        public Long getNonLues() {
            return nonLues;
        }
        
        public void setNonLues(Long nonLues) {
            this.nonLues = nonLues;
        }
        
        public Long getLues() {
            return lues;
        }
        
        public void setLues(Long lues) {
            this.lues = lues;
        }
    }
}
