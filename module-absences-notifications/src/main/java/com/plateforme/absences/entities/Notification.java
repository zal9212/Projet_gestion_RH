package com.plateforme.absences.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité JPA pour les notifications utilisateurs.
 * Table : notifications
 */
@Entity
@Table(name = "notifications")
@NamedQueries({
    @NamedQuery(
        name = "Notification.findByDestinataire",
        query = "SELECT n FROM Notification n WHERE n.destinataireId = :userId ORDER BY n.dateEnvoi DESC"
    ),
    @NamedQuery(
        name = "Notification.findNonLues",
        query = "SELECT n FROM Notification n WHERE n.destinataireId = :userId AND n.lue = false ORDER BY n.dateEnvoi DESC"
    ),
    @NamedQuery(
        name = "Notification.countNonLues",
        query = "SELECT COUNT(n) FROM Notification n WHERE n.destinataireId = :userId AND n.lue = false"
    )
})
public class Notification implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "destinataire_id", nullable = false)
    private Long destinataireId;
    
    @Column(name = "expediteur_id")
    private Long expediteurId;
    
    @Column(nullable = false, length = 50)
    private String type;
    
    @Column(nullable = false)
    private String sujet;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "reference_id")
    private Long referenceId;
    
    @Column(name = "type_reference", length = 50)
    private String typeReference;
    
    @Column(nullable = false)
    private Boolean lue = false;
    
    @Column(name = "date_envoi", updatable = false)
    private LocalDateTime dateEnvoi;
    
    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    // Constructeurs
    public Notification() {
    }

    public Notification(Long destinataireId, String type, String sujet, String message) {
        this.destinataireId = destinataireId;
        this.type = type;
        this.sujet = sujet;
        this.message = message;
        this.lue = false;
    }

    public Notification(Long id, Long destinataireId, Long expediteurId, String type, String sujet, String message, Long referenceId, String typeReference, Boolean lue, LocalDateTime dateEnvoi, LocalDateTime dateLecture) {
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
    }

    // JPA Callbacks
    @PrePersist
    protected void onCreate() {
        dateEnvoi = LocalDateTime.now();
    }
    
    /**
     * Marque la notification comme lue
     */
    public void marquerCommeLue() {
        this.lue = true;
        this.dateLecture = LocalDateTime.now();
    }
    
    /**
     * Vérifie si la notification est récente (moins de 24h)
     */
    public boolean estRecente() {
        if (dateEnvoi == null) return false;
        return dateEnvoi.isAfter(LocalDateTime.now().minusDays(1));
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
}
