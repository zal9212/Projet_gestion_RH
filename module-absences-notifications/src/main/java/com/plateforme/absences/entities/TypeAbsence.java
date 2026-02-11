package com.plateforme.absences.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité JPA pour les types d'absences.
 * Table : types_absences
 */
@Entity
@Table(name = "types_absences")
@NamedQueries({
    @NamedQuery(
        name = "TypeAbsence.findAll",
        query = "SELECT t FROM TypeAbsence t ORDER BY t.nom"
    ),
    @NamedQuery(
        name = "TypeAbsence.findByNom",
        query = "SELECT t FROM TypeAbsence t WHERE t.nom = :nom"
    )
})
public class TypeAbsence implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String nom;
    
    @Column(length = 255)
    private String description;
    
    @Column(name = "justification_requise")
    private Boolean justificationRequise = true;
    
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    // Constructeurs
    public TypeAbsence() {
    }

    public TypeAbsence(String nom, String description, Boolean justificationRequise) {
        this.nom = nom;
        this.description = description;
        this.justificationRequise = justificationRequise;
    }

    public TypeAbsence(Long id, String nom, String description, Boolean justificationRequise, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.justificationRequise = justificationRequise;
        this.dateCreation = dateCreation;
    }

    // JPA Callbacks
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getJustificationRequise() { return justificationRequise; }
    public void setJustificationRequise(Boolean justificationRequise) { this.justificationRequise = justificationRequise; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
