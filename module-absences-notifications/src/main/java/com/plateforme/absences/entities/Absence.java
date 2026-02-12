package com.plateforme.absences.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Entité JPA pour les absences des employés.
 * Table : absences
 */
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
    
    @Column(name = "employe_id", nullable = false)
    private Long employeId;
    
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
    
    @Column(length = 20)
    private String statut = "EN_ATTENTE";
    
    @Column(nullable = false)
    private Boolean justifiee = false;
    
    @Column(name = "document_justificatif")
    private String documentJustificatif;
    
    @Column(name = "enregistre_par")
    private Long enregistrePar;
    
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    // Constructeurs
    public Absence() {
    }

    public Absence(Long employeId, TypeAbsence typeAbsence, LocalDate dateAbsence) {
        this.employeId = employeId;
        this.typeAbsence = typeAbsence;
        this.dateAbsence = dateAbsence;
        this.justifiee = false;
    }

    public Absence(Long id, Long employeId, TypeAbsence typeAbsence, LocalDate dateAbsence, LocalTime heureDebut, LocalTime heureFin, BigDecimal dureeHeures, String motif, Boolean justifiee, String documentJustificatif, Long enregistrePar, LocalDateTime dateCreation) {
        this.id = id;
        this.employeId = employeId;
        this.typeAbsence = typeAbsence;
        this.dateAbsence = dateAbsence;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.dureeHeures = dureeHeures;
        this.motif = motif;
        this.justifiee = justifiee;
        this.documentJustificatif = documentJustificatif;
        this.enregistrePar = enregistrePar;
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

    public Long getEmployeId() { return employeId; }
    public void setEmployeId(Long employeId) { this.employeId = employeId; }

    public TypeAbsence getTypeAbsence() { return typeAbsence; }
    public void setTypeAbsence(TypeAbsence typeAbsence) { this.typeAbsence = typeAbsence; }

    public LocalDate getDateAbsence() { return dateAbsence; }
    public void setDateAbsence(LocalDate dateAbsence) { this.dateAbsence = dateAbsence; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public BigDecimal getDureeHeures() { return dureeHeures; }
    public void setDureeHeures(BigDecimal dureeHeures) { this.dureeHeures = dureeHeures; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public Boolean getJustifiee() { return justifiee; }
    public void setJustifiee(Boolean justifiee) { this.justifiee = justifiee; }

    public String getDocumentJustificatif() { return documentJustificatif; }
    public void setDocumentJustificatif(String documentJustificatif) { this.documentJustificatif = documentJustificatif; }

    public Long getEnregistrePar() { return enregistrePar; }
    public void setEnregistrePar(Long enregistrePar) { this.enregistrePar = enregistrePar; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
