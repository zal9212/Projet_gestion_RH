package com.plateforme.absences.dto;

import com.plateforme.absences.entities.Notification;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) pour les notifications.
 * Utilisé pour les réponses API REST.
 */
public class NotificationDTO {
    
    private Long id;
    private Long destinataireId;
    private Long expediteurId;
    private String type;
    private String sujet;
    private String message;
    private Long referenceId;
    private String typeReference;
    private Boolean lue;
    private LocalDateTime dateEnvoi;
    private LocalDateTime dateLecture;
    private Boolean recente;

    // Constructeurs
    public NotificationDTO() {
    }

    public NotificationDTO(Long id, Long destinataireId, Long expediteurId, String type, String sujet, String message, Long referenceId, String typeReference, Boolean lue, LocalDateTime dateEnvoi, LocalDateTime dateLecture, Boolean recente) {
        this.id = id;
        this.destinataireId = destinataireId;
        this.expediteurId = expediteurId;
        this.type = type;
        this.sujet = sujet;
        this.message = message;
        this.referenceId = referenceId;
        this.typeReference = typeReference;
        this.lue = lue;
        this.dateEnvoi = dateEnvoi;
        this.dateLecture = dateLecture;
        this.recente = recente;
    }

    /**
     * Constructeur depuis une entité Notification.
     */
    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.destinataireId = notification.getDestinataireId();
        this.expediteurId = notification.getExpediteurId();
        this.type = notification.getType();
        this.sujet = notification.getSujet();
        this.message = notification.getMessage();
        this.referenceId = notification.getReferenceId();
        this.typeReference = notification.getTypeReference();
        this.lue = notification.getLue();
        this.dateEnvoi = notification.getDateEnvoi();
        this.dateLecture = notification.getDateLecture();
        this.recente = notification.estRecente();
    }
    
    /**
     * Constructeur simplifié pour création rapide.
     */
    public NotificationDTO(String type, String sujet, String message) {
        this.type = type;
        this.sujet = sujet;
        this.message = message;
        this.lue = false;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }

    public Long getExpediteurId() { return expediteurId; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public String getTypeReference() { return typeReference; }
    public void setTypeReference(String typeReference) { this.typeReference = typeReference; }

    public Boolean getLue() { return lue; }
    public void setLue(Boolean lue) { this.lue = lue; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public LocalDateTime getDateLecture() { return dateLecture; }
    public void setDateLecture(LocalDateTime dateLecture) { this.dateLecture = dateLecture; }

    public Boolean getRecente() { return recente; }
    public void setRecente(Boolean recente) { this.recente = recente; }
}
