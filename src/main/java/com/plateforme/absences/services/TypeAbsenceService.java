public class TypeAbsenceService {
    
}
package com.plateforme.absences.services;

import com.plateforme.absences.entities.TypeAbsence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service EJB pour gérer les types d'absences.
 */
@Stateless
public class TypeAbsenceService {
    
    private static final Logger LOGGER = Logger.getLogger(TypeAbsenceService.class.getName());
    
    @PersistenceContext(unitName = "absencesPU")
    private EntityManager em;
    
    /**
     * Récupère tous les types d'absences.
     */
    public List<TypeAbsence> findAll() {
        LOGGER.info("Récupération de tous les types d'absences");
        TypedQuery<TypeAbsence> query = em.createNamedQuery("TypeAbsence.findAll", TypeAbsence.class);
        return query.getResultList();
    }
    
    /**
     * Trouve un type par son ID.
     */
    public TypeAbsence findById(Long id) {
        LOGGER.info("Recherche du type d'absence avec ID : " + id);
        return em.find(TypeAbsence.class, id);
    }
    
    /**
     * Trouve un type par son nom.
     */
    public TypeAbsence findByNom(String nom) {
        LOGGER.info("Recherche du type d'absence : " + nom);
        TypedQuery<TypeAbsence> query = em.createNamedQuery("TypeAbsence.findByNom", TypeAbsence.class);
        query.setParameter("nom", nom);
        List<TypeAbsence> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
    
    /**
     * Crée un nouveau type d'absence.
     */
    public TypeAbsence create(TypeAbsence type) {
        LOGGER.info("Création d'un nouveau type d'absence : " + type.getNom());
        em.persist(type);
        return type;
    }
    
    /**
     * Met à jour un type d'absence.
     */
    public TypeAbsence update(TypeAbsence type) {
        LOGGER.info("Mise à jour du type d'absence ID : " + type.getId());
        return em.merge(type);
    }
    
    /**
     * Supprime un type d'absence.
     */
    public void delete(Long id) {
        LOGGER.info("Suppression du type d'absence ID : " + id);
        TypeAbsence type = findById(id);
        if (type != null) {
            em.remove(type);
        }
    }
}
