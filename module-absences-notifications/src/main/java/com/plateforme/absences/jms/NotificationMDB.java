package com.plateforme.absences.jms;

import com.plateforme.absences.entities.Notification;
import com.plateforme.absences.services.NotificationService;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Message-Driven Bean pour traiter les notifications de manière asynchrone.
 * 
 * Ce bean écoute la queue JMS "NotificationQueue" et crée des notifications
 * en base de données quand il reçoit des messages.
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(
            propertyName = "destinationLookup",
            propertyValue = "java:/jms/queue/NotificationQueue"
        ),
        @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue"
        )
    }
)
public class NotificationMDB implements MessageListener {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationMDB.class.getName());
    
    @EJB
    private NotificationService notificationService;
    
    /**
     * Méthode appelée automatiquement quand un message arrive dans la queue.
     * 
     * @param message Le message JMS reçu
     */
    @Override
    public void onMessage(Message message) {
        LOGGER.info("Message JMS reçu dans NotificationMDB");
        
        try {
            // Vérifier que c'est un ObjectMessage
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Object object = objectMessage.getObject();
                
                // Vérifier que c'est un NotificationEvent
                if (object instanceof NotificationEvent) {
                    NotificationEvent event = (NotificationEvent) object;
                    
                    LOGGER.info("Traitement de l'événement : " + event);
                    
                    // Créer la notification en base de données
                    creerNotification(event);
                    
                } else {
                    LOGGER.warning("Type de message non supporté : " + object.getClass().getName());
                }
            } else {
                LOGGER.warning("Type de message JMS non supporté : " + message.getClass().getName());
            }
            
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement du message JMS", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur inattendue dans NotificationMDB", e);
        }
    }
    
    /**
     * Crée une notification à partir d'un événement.
     * 
     * @param event L'événement de notification
     */
    private void creerNotification(NotificationEvent event) {
        try {
            Notification notification = new Notification();
            notification.setDestinataireId(event.getDestinataireId());
            notification.setExpediteurId(event.getExpediteurId());
            notification.setType(event.getType());
            notification.setSujet(event.getSujet());
            notification.setMessage(event.getMessage());
            notification.setReferenceId(event.getReferenceId());
            notification.setTypeReference(event.getTypeReference());
            
            notificationService.create(notification);
            
            LOGGER.info("Notification créée avec succès pour l'utilisateur : " + event.getDestinataireId());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de la notification", e);
        }
    }
}
