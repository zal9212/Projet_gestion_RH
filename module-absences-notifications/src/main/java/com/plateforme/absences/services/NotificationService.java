package com.plateforme.absences.services;

import com.plateforme.absences.entities.Notification;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service EJB pour gérer les notifications.
 * 
 * Gère la création, lecture, mise à jour et suppression des notifications.
 * Fournit des méthodes utilitaires pour créer des notifications spécifiques.
 */
@Stateless
public class NotificationService {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    
    @PersistenceContext(unitName = "absencesPU")
    private EntityManager em;
    
    // ========================================
    // MÉTHODES DE RECHERCHE
    // ========================================
    
    /**
     * Récupère toutes les notifications d'un utilisateur.
     * 
     * @param userId ID de l'utilisateur destinataire
     * @return Liste des notifications (triées par date décroissante)
     */
    public List<Notification> findByDestinataire(Long userId) {
        LOGGER.info("Récupération des notifications pour l'utilisateur ID : " + userId);
        TypedQuery<Notification> query = em.createNamedQuery(
            "Notification.findByDestinataire", 
            Notification.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    /**
     * Récupère les notifications non lues d'un utilisateur.
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des notifications non lues
     */
    public List<Notification> findNonLues(Long userId) {
        LOGGER.info("Récupération des notifications non lues pour l'utilisateur ID : " + userId);
        TypedQuery<Notification> query = em.createNamedQuery(
            "Notification.findNonLues", 
            Notification.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    /**
     * Compte le nombre de notifications non lues.
     * 
     * @param userId ID de l'utilisateur
     * @return Nombre de notifications non lues
     */
    public Long countNonLues(Long userId) {
        LOGGER.info("Comptage des notifications non lues pour l'utilisateur ID : " + userId);
        TypedQuery<Long> query = em.createNamedQuery(
            "Notification.countNonLues", 
            Long.class
        );
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }
    
    /**
     * Trouve une notification par son ID.
     * 
     * @param id ID de la notification
     * @return La notification ou null
     */
    public Notification findById(Long id) {
        LOGGER.info("Recherche de la notification ID : " + id);
        return em.find(Notification.class, id);
    }
    
    /**
     * Récupère les notifications récentes (dernières 24h).
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des notifications récentes
     */
    public List<Notification> findRecentes(Long userId) {
        LOGGER.info("Récupération des notifications récentes pour l'utilisateur ID : " + userId);
        
        String jpql = "SELECT n FROM Notification n " +
                     "WHERE n.destinataireId = :userId " +
                     "AND n.dateEnvoi >= CURRENT_TIMESTAMP - 1 " +
                     "ORDER BY n.dateEnvoi DESC";
        
        TypedQuery<Notification> query = em.createQuery(jpql, Notification.class);
        query.setParameter("userId", userId);
        query.setMaxResults(10); // Limite à 10 notifications
        
        return query.getResultList();
    }
    
    // ========================================
    // MÉTHODES DE CRÉATION
    // ========================================
    
    /**
     * Crée une nouvelle notification.
     * 
     * @param notification La notification à créer
     * @return La notification créée avec son ID
     */
    public Notification create(Notification notification) {
        LOGGER.info("Création d'une notification pour l'utilisateur ID : " 
                   + notification.getDestinataireId());
        em.persist(notification);
        em.flush(); // Force l'insertion pour obtenir l'ID
        return notification;
    }
    
    /**
     * Crée une notification pour une absence enregistrée.
     * 
     * @param destinataireId ID de l'employé concerné
     * @param expediteurId ID du manager (peut être null)
     * @param absenceId ID de l'absence
     * @param typeAbsence Nom du type d'absence
     * @param dateAbsence Date de l'absence formatée
     * @return La notification créée
     */
    public Notification creerNotificationAbsence(
            Long destinataireId, 
            Long expediteurId,
            Long absenceId, 
            String typeAbsence,
            String dateAbsence) {
        
        LOGGER.info("Création notification absence pour employé ID : " + destinataireId);
        
        Notification notification = new Notification();
        notification.setDestinataireId(destinataireId);
        notification.setExpediteurId(expediteurId);
        notification.setType("ABSENCE");
        notification.setSujet("Absence enregistrée");
        notification.setMessage(
            String.format("Votre absence (%s) du %s a été enregistrée.", 
                         typeAbsence, dateAbsence)
        );
        notification.setReferenceId(absenceId);
        notification.setTypeReference("ABSENCE");
        
        return create(notification);
    }
    
    /**
     * Crée une notification pour un congé (approuvé/rejeté).
     * 
     * @param destinataireId ID de l'employé
     * @param expediteurId ID du validateur
     * @param congeId ID de la demande de congé
     * @param statut "APPROUVE" ou "REJETE"
     * @param periode Période du congé (ex: "10/02 au 14/02")
     * @param commentaire Commentaire du manager (optionnel)
     * @return La notification créée
     */
    public Notification creerNotificationConge(
            Long destinataireId, 
            Long expediteurId,
            Long congeId, 
            String statut,
            String periode,
            String commentaire) {
        
        LOGGER.info("Création notification congé " + statut + " pour employé ID : " + destinataireId);
        
        Notification notification = new Notification();
        notification.setDestinataireId(destinataireId);
        notification.setExpediteurId(expediteurId);
        
        if ("APPROUVE".equals(statut)) {
            notification.setType("CONGE_APPROUVE");
            notification.setSujet("Demande de congé approuvée");
            notification.setMessage(
                String.format("Votre demande de congé du %s a été approuvée.", periode)
            );
        } else if ("REJETE".equals(statut)) {
            notification.setType("CONGE_REJETE");
            notification.setSujet("Demande de congé rejetée");
            notification.setMessage(
                String.format("Votre demande de congé du %s a été rejetée. Raison : %s", 
                             periode, 
                             commentaire != null ? commentaire : "Non précisée")
            );
        } else {
            notification.setType("DEMANDE_CONGE");
            notification.setSujet("Nouvelle demande de congé");
            notification.setMessage(
                String.format("Une demande de congé du %s est en attente de validation.", periode)
            );
        }
        
        notification.setReferenceId(congeId);
        notification.setTypeReference("DEMANDE_CONGE");
        
        return create(notification);
    }
    
    /**
     * Crée une notification générique.
     * 
     * @param destinataireId ID du destinataire
     * @param expediteurId ID de l'expéditeur (peut être null pour système)
     * @param type Type de notification
     * @param sujet Sujet
     * @param message Contenu du message
     * @return La notification créée
     */
    public Notification creerNotification(
            Long destinataireId,
            Long expediteurId,
            String type,
            String sujet,
            String message) {
        
        LOGGER.info("Création notification type " + type + " pour utilisateur ID : " + destinataireId);
        
        Notification notification = new Notification();
        notification.setDestinataireId(destinataireId);
        notification.setExpediteurId(expediteurId);
        notification.setType(type);
        notification.setSujet(sujet);
        notification.setMessage(message);
        
        return create(notification);
    }
    
    // ========================================
    // MÉTHODES DE MISE À JOUR
    // ========================================
    
    /**
     * Marque une notification comme lue.
     * 
     * @param notificationId ID de la notification
     * @return La notification mise à jour ou null
     */
    public Notification marquerCommeLue(Long notificationId) {
        LOGGER.info("Marquage de la notification ID " + notificationId + " comme lue");
        
        Notification notification = findById(notificationId);
        if (notification != null && !notification.getLue()) {
            notification.marquerCommeLue(); // Utilise la méthode de l'entité
            return em.merge(notification);
        }
        return notification;
    }
    
    /**
     * Marque toutes les notifications d'un utilisateur comme lues.
     * 
     * @param userId ID de l'utilisateur
     * @return Nombre de notifications marquées
     */
    public int marquerToutesCommeLues(Long userId) {
        LOGGER.info("Marquage de toutes les notifications comme lues pour utilisateur ID : " + userId);
        
        String jpql = "UPDATE Notification n " +
                     "SET n.lue = true, n.dateLecture = CURRENT_TIMESTAMP " +
                     "WHERE n.destinataireId = :userId AND n.lue = false";
        
        return em.createQuery(jpql)
                 .setParameter("userId", userId)
                 .executeUpdate();
    }
    
    // ========================================
    // MÉTHODES DE SUPPRESSION
    // ========================================
    
    /**
     * Supprime une notification.
     * 
     * @param id ID de la notification
     * @return true si supprimée, false sinon
     */
    public boolean delete(Long id) {
        LOGGER.info("Suppression de la notification ID : " + id);
        
        Notification notification = findById(id);
        if (notification != null) {
            em.remove(notification);
            return true;
        }
        return false;
    }
    
    /**
     * Supprime toutes les notifications d'un utilisateur.
     * 
     * @param userId ID de l'utilisateur
     * @return Nombre de notifications supprimées
     */
    public int deleteByDestinataire(Long userId) {
        LOGGER.info("Suppression de toutes les notifications pour utilisateur ID : " + userId);
        
        String jpql = "DELETE FROM Notification n WHERE n.destinataireId = :userId";
        
        return em.createQuery(jpql)
                 .setParameter("userId", userId)
                 .executeUpdate();
    }
    
    /**
     * Supprime les notifications lues de plus de 30 jours.
     * 
     * @return Nombre de notifications supprimées
     */
    public int deleteAnciennesNotificationsLues() {
        LOGGER.info("Suppression des anciennes notifications lues (> 30 jours)");
        
        String jpql = "DELETE FROM Notification n " +
                     "WHERE n.lue = true " +
                     "AND n.dateLecture < CURRENT_TIMESTAMP - 30";
        
        return em.createQuery(jpql).executeUpdate();
    }
    
    // ========================================
    // MÉTHODES STATISTIQUES
    // ========================================
    
    /**
     * Récupère les statistiques de notifications d'un utilisateur.
     * 
     * @param userId ID de l'utilisateur
     * @return Tableau [total, non_lues, lues]
     */
    public Long[] getStatistiques(Long userId) {
        LOGGER.info("Récupération des statistiques pour utilisateur ID : " + userId);
        
        Long total = em.createQuery(
            "SELECT COUNT(n) FROM Notification n WHERE n.destinataireId = :userId", 
            Long.class
        ).setParameter("userId", userId).getSingleResult();
        
        Long nonLues = countNonLues(userId);
        Long lues = total - nonLues;
        
        return new Long[]{total, nonLues, lues};
    }
}
