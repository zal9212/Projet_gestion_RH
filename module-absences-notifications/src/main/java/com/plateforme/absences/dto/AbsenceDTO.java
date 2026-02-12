package com.plateforme.absences.dto;

import com.plateforme.absences.entities.Absence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * DTO pour les absences.
 */
public class AbsenceDTO {
    
    private Long id;
    private Long employeId;
    private Long typeAbsenceId;
    private String typeAbsenceNom;
    private LocalDate dateAbsence;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private BigDecimal dureeHeures;
    private String motif;
    private Boolean justifiee;
    private String documentJustificatif;
    private Long enregistrePar;
    private String statut;
    private LocalDateTime dateCreation;

    // Constructeurs
    public AbsenceDTO() {
    }

    public AbsenceDTO(Long id, Long employeId, Long typeAbsenceId, String typeAbsenceNom, LocalDate dateAbsence, LocalTime heureDebut, LocalTime heureFin, BigDecimal dureeHeures, String motif, Boolean justifiee, String documentJustificatif, Long enregistrePar, String statut, LocalDateTime dateCreation) {
        this.id = id;
        this.employeId = employeId;
        this.typeAbsenceId = typeAbsenceId;
        this.typeAbsenceNom = typeAbsenceNom;
        this.dateAbsence = dateAbsence;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.dureeHeures = dureeHeures;
        this.motif = motif;
        this.justifiee = justifiee;
        this.documentJustificatif = documentJustificatif;
        this.enregistrePar = enregistrePar;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    /**
     * Constructeur depuis une entité Absence.
     */
    public AbsenceDTO(Absence absence) {
        this.id = absence.getId();
        this.employeId = absence.getEmployeId();
        this.typeAbsenceId = absence.getTypeAbsence().getId();
        this.typeAbsenceNom = absence.getTypeAbsence().getNom();
        this.dateAbsence = absence.getDateAbsence();
        this.heureDebut = absence.getHeureDebut();
        this.heureFin = absence.getHeureFin();
        this.dureeHeures = absence.getDureeHeures();
        this.motif = absence.getMotif();
        this.justifiee = absence.getJustifiee();
        this.documentJustificatif = absence.getDocumentJustificatif();
        this.enregistrePar = absence.getEnregistrePar();
        this.statut = absence.getStatut();
        this.dateCreation = absence.getDateCreation();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeId() { return employeId; }
    public void setEmployeId(Long employeId) { this.employeId = employeId; }

    public Long getTypeAbsenceId() { return typeAbsenceId; }
    public void setTypeAbsenceId(Long typeAbsenceId) { this.typeAbsenceId = typeAbsenceId; }

    public String getTypeAbsenceNom() { return typeAbsenceNom; }
    public void setTypeAbsenceNom(String typeAbsenceNom) { this.typeAbsenceNom = typeAbsenceNom; }

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
