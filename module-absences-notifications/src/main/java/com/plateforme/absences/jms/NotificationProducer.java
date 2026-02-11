package com.plateforme.absences.jms;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service pour envoyer des messages JMS vers la queue de notifications.
 * 
 * Utilisé pour déclencher l'envoi asynchrone de notifications.
 */
@Stateless
public class NotificationProducer {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationProducer.class.getName());
    
    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup = "java:/jms/queue/NotificationQueue")
    private Queue notificationQueue;
    
    /**
     * Envoie un événement de notification dans la queue JMS.
     * 
     * @param event L'événement à envoyer
     */
    public void envoyerNotification(NotificationEvent event) {
        LOGGER.info("Envoi d'un événement JMS : " + event);
        
        Connection connection = null;
        Session session = null;
        
        try {
            // Créer la connexion JMS
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            // Créer le producer
            MessageProducer producer = session.createProducer(notificationQueue);
            
            // Créer le message
            ObjectMessage message = session.createObjectMessage(event);
            
            // Envoyer le message
            producer.send(message);
            
            LOGGER.info("Événement JMS envoyé avec succès");
            
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'envoi du message JMS", e);
        } finally {
            // Fermer les ressources
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture des ressources JMS", e);
            }
        }
    }
    
    /**
     * Envoie une notification d'absence enregistrée.
     */
    public void envoyerNotificationAbsence(Long employeId, Long managerId, Long absenceId, 
                                           String typeAbsence, String dateAbsence) {
        NotificationEvent event = new NotificationEvent();
        event.setDestinataireId(employeId);
        event.setExpediteurId(managerId);
        event.setType("ABSENCE");
        event.setSujet("Absence enregistrée");
        event.setMessage(String.format("Votre absence (%s) du %s a été enregistrée.", 
                                       typeAbsence, dateAbsence));
        event.setReferenceId(absenceId);
        event.setTypeReference("ABSENCE");
        
        envoyerNotification(event);
    }
    
    /**
     * Envoie une notification de congé approuvé.
     */
    public void envoyerNotificationCongeApprouve(Long employeId, Long managerId, 
                                                 Long congeId, String periode) {
        NotificationEvent event = new NotificationEvent();
        event.setDestinataireId(employeId);
        event.setExpediteurId(managerId);
        event.setType("CONGE_APPROUVE");
        event.setSujet("Demande de congé approuvée");
        event.setMessage(String.format("Votre demande de congé du %s a été approuvée.", periode));
        event.setReferenceId(congeId);
        event.setTypeReference("DEMANDE_CONGE");
        
        envoyerNotification(event);
    }
    
    /**
     * Envoie une notification de congé rejeté.
     */
    public void envoyerNotificationCongeRejete(Long employeId, Long managerId, 
                                               Long congeId, String periode, String raison) {
        NotificationEvent event = new NotificationEvent();
        event.setDestinataireId(employeId);
        event.setExpediteurId(managerId);
        event.setType("CONGE_REJETE");
        event.setSujet("Demande de congé rejetée");
        event.setMessage(String.format("Votre demande de congé du %s a été rejetée. Raison : %s", 
                                       periode, raison));
        event.setReferenceId(congeId);
        event.setTypeReference("DEMANDE_CONGE");
        
        envoyerNotification(event);
    }
}
