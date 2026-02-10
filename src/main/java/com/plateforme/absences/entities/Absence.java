package com.plateforme.absences.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Entité JPA pour les absences des employés.
 * Table : absences
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "absences")
@NamedQueries({
    @NamedQuery(
        name = "Absence.findByEmploye",
        query = "SELECT a FROM Absence a WHERE a.employeId = :employeId ORDER BY a.dateAbsence DESC"
    ),
    @NamedQuery(
        name = "Absence.findByPeriode",
        query = "SELECT a FROM Absence a WHERE a.dateAbsence BETWEEN :debut AND :fin ORDER BY a.dateAbsence"
    ),
    @NamedQuery(
        name = "Absence.findNonJustifiees",
        query = "SELECT a FROM Absence a WHERE a.justifiee = false AND a.typeAbsence.justificationRequise = true"
    ),
    @NamedQuery(
        name = "Absence.findByEquipe",
        query = "SELECT a FROM Absence a WHERE a.employeId IN :employeIds ORDER BY a.dateAbsence DESC"
    )
})
public class Absence implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * ID de l'employé (relation gérée par le module Employés)
     */
    @Column(name = "employe_id", nullable = false)
    private Long employeId;
    
    /**
     * Type d'absence (relation JPA)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_absence_id", nullable = false)
    private TypeAbsence typeAbsence;
    
    @Column(name = "date_absence", nullable = false)
    private LocalDate dateAbsence;
    
    @Column(name = "heure_debut")
    private LocalTime heureDebut;
    
    @Column(name = "heure_fin")
    private LocalTime heureFin;
    
    @Column(name = "duree_heures", precision = 4, scale = 2)
    private BigDecimal dureeHeures;
    
    @Column(columnDefinition = "TEXT")
    private String motif;
    
    @Column(nullable = false)
    private Boolean justifiee = false;
    
    @Column(name = "document_justificatif")
    private String documentJustificatif;
    
    /**
     * ID du manager qui a enregistré l'absence
     */
    @Column(name = "enregistre_par")
    private Long enregistrePar;
    
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
    
    // Constructeur personnalisé
    public Absence(Long employeId, TypeAbsence typeAbsence, LocalDate dateAbsence) {
        this.employeId = employeId;
        this.typeAbsence = typeAbsence;
        this.dateAbsence = dateAbsence;
        this.justifiee = false;
    }
}
