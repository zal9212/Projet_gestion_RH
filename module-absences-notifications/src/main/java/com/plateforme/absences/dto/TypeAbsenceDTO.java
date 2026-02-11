package com.plateforme.absences.dto;

import com.plateforme.absences.entities.TypeAbsence;

/**
 * DTO pour les types d'absences.
 */
public class TypeAbsenceDTO {
    
    private Long id;
    private String nom;
    private String description;
    private Boolean justificationRequise;

    // Constructeurs
    public TypeAbsenceDTO() {
    }

    public TypeAbsenceDTO(Long id, String nom, String description, Boolean justificationRequise) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.justificationRequise = justificationRequise;
    }

    /**
     * Constructeur depuis une entité TypeAbsence.
     */
    public TypeAbsenceDTO(TypeAbsence type) {
        this.id = type.getId();
        this.nom = type.getNom();
        this.description = type.getDescription();
        this.justificationRequise = type.getJustificationRequise();
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
}
