public class Notification {
    
}
package com.plateforme.absences.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité JPA pour les notifications utilisateurs.
 * Table : notifications
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    // Constructeur personnalisé
    public Notification(Long destinataireId, String type, String sujet, String message) {
        this.destinataireId = destinataireId;
        this.type = type;
        this.sujet = sujet;
        this.message = message;
        this.lue = false;
    }
}