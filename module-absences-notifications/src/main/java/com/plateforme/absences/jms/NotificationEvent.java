package com.plateforme.absences.jms;

import java.io.Serializable;

/**
 * Événement JMS pour déclencher l'envoi de notifications.
 * 
 * Cet objet est sérialisé et envoyé via JMS Queue.
 */
public class NotificationEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long destinataireId;
    private Long expediteurId;
    private String type;
    private String sujet;
    private String message;
    private Long referenceId;
    private String typeReference;
    
    // ========================================
    // CONSTRUCTEURS
    // ========================================
    
    public NotificationEvent() {
    }
    
    public NotificationEvent(Long destinataireId, String type, String sujet, String message) {
        this.destinataireId = destinataireId;
        this.type = type;
        this.sujet = sujet;
        this.message = message;
    }
    
    // ========================================
    // GETTERS ET SETTERS
    // ========================================
    
    public Long getDestinataireId() {
        return destinataireId;
    }

    public void setDestinataireId(Long destinataireId) {
        this.destinataireId = destinataireId;
    }

    public Long getExpediteurId() {
        return expediteurId;
    }

    public void setExpediteurId(Long expediteurId) {
        this.expediteurId = expediteurId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(String typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "destinataireId=" + destinataireId +
                ", type='" + type + '\'' +
                ", sujet='" + sujet + '\'' +
                '}';
    }
}
