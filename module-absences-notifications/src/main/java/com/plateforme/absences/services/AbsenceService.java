package com.plateforme.absences.services;

import com.plateforme.absences.entities.Absence;
import com.plateforme.absences.jms.NotificationProducer;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service EJB pour gérer les absences.
 */
@Stateless
public class AbsenceService {
    
    private static final Logger LOGGER = Logger.getLogger(AbsenceService.class.getName());
    
    @PersistenceContext(unitName = "absencesPU")
    private EntityManager em;
    
    @EJB
    private NotificationProducer notificationProducer;
    
    /**
     * Récupère toutes les absences.
     */
    public List<Absence> findAll() {
        LOGGER.info("Récupération de toutes les absences");
        return em.createQuery("SELECT a FROM Absence a ORDER BY a.dateAbsence DESC", Absence.class)
                 .getResultList();
    }
    
    /**
     * Trouve une absence par son ID.
     */
    public Absence findById(Long id) {
        LOGGER.info("Recherche de l'absence ID : " + id);
        return em.find(Absence.class, id);
    }
    
    /**
     * Trouve les absences d'un employé.
     */
    public List<Absence> findByEmploye(Long employeId) {
        LOGGER.info("Recherche des absences de l'employé ID : " + employeId);
        TypedQuery<Absence> query = em.createNamedQuery("Absence.findByEmploye", Absence.class);
        query.setParameter("employeId", employeId);
        return query.getResultList();
    }
    
    /**
     * Trouve les absences sur une période.
     */
    public List<Absence> findByPeriode(LocalDate debut, LocalDate fin) {
        LOGGER.info("Recherche des absences entre " + debut + " et " + fin);
        TypedQuery<Absence> query = em.createNamedQuery("Absence.findByPeriode", Absence.class);
        query.setParameter("debut", debut);
        query.setParameter("fin", fin);
        return query.getResultList();
    }
    
    /**
     * Trouve les absences d'une équipe (liste d'employés).
     */
    public List<Absence> findByEquipe(List<Long> employeIds) {
        LOGGER.info("Recherche des absences de l'équipe");
        TypedQuery<Absence> query = em.createNamedQuery("Absence.findByEquipe", Absence.class);
        query.setParameter("employeIds", employeIds);
        return query.getResultList();
    }
    
    /**
     * Trouve les absences non justifiées.
     */
    public List<Absence> findNonJustifiees() {
        LOGGER.info("Recherche des absences non justifiées");
        return em.createNamedQuery("Absence.findNonJustifiees", Absence.class)
                 .getResultList();
    }
    
    /**
     * Crée une nouvelle absence ET envoie une notification asynchrone.
     */
    public Absence create(Absence absence) {
        LOGGER.info("Création d'une absence pour l'employé ID : " + absence.getEmployeId());
        
        // 1. Persister l'absence
        em.persist(absence);
        em.flush(); // Force l'INSERT pour avoir l'ID
        
        // 2. Envoyer une notification JMS asynchrone
        try {
            notificationProducer.envoyerNotificationAbsence(
                absence.getEmployeId(),
                absence.getEnregistrePar(),
                absence.getId(),
                absence.getTypeAbsence().getNom(),
                absence.getDateAbsence().toString()
            );
        } catch (Exception e) {
            LOGGER.warning("Erreur lors de l'envoi de la notification JMS : " + e.getMessage());
            // On ne bloque pas si la notification échoue
        }
        
        return absence;
    }
    
    /**
     * Met à jour une absence.
     */
    public Absence update(Absence absence) {
        LOGGER.info("Mise à jour de l'absence ID : " + absence.getId());
        return em.merge(absence);
    }
    
    /**
     * Supprime une absence.
     */
    public void delete(Long id) {
        LOGGER.info("Suppression de l'absence ID : " + id);
        Absence absence = findById(id);
        if (absence != null) {
            em.remove(absence);
        }
    }
    
    /**
     * Justifie une absence avec un document.
     */
    public Absence justifier(Long id, String documentPath) {
        LOGGER.info("Justification de l'absence ID : " + id);
        Absence absence = findById(id);
        if (absence != null) {
            absence.setJustifiee(true);
            absence.setDocumentJustificatif(documentPath);
            return em.merge(absence);
        }
        return null;
    }
}
