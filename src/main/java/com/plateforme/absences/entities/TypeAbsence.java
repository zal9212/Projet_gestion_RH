package com.plateforme.absences.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité JPA pour les types d'absences.
 * Table : types_absences
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
    
    // Constructeur personnalisé pour faciliter l'initialisation
    public TypeAbsence(String nom, String description, Boolean justificationRequise) {
        this.nom = nom;
        this.description = description;
        this.justificationRequise = justificationRequise;
    }
}
