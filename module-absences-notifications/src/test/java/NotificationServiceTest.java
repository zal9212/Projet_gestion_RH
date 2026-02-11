import com.plateforme.absences.entities.Notification;
import com.plateforme.absences.services.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {
    
    @Mock
    private EntityManager em;
    
    @Mock
    private TypedQuery<Notification> query;
    
    @InjectMocks
    private NotificationService notificationService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCreate() {
        // Arrange
        Notification notification = new Notification(1L, "TEST", "Sujet test", "Message test");
        
        // Act
        notificationService.create(notification);
        
        // Assert
        verify(em, times(1)).persist(notification);
        verify(em, times(1)).flush();
    }
    
    @Test
    public void testFindByDestinataire() {
        // Arrange
        Long userId = 1L;
        List<Notification> expectedNotifications = Arrays.asList(
            new Notification(userId, "TYPE1", "Sujet 1", "Message 1"),
            new Notification(userId, "TYPE2", "Sujet 2", "Message 2")
        );
        
        when(em.createNamedQuery("Notification.findByDestinataire", Notification.class))
            .thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedNotifications);
        
        // Act
        List<Notification> result = notificationService.findByDestinataire(userId);
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(userId, result.get(0).getDestinataireId());
    }
    
    @Test
    public void testMarquerCommeLue() {
        // Arrange
        Long notifId = 1L;
        Notification notification = new Notification(1L, "TEST", "Sujet", "Message");
        notification.setId(notifId);
        notification.setLue(false);
        
        when(em.find(Notification.class, notifId)).thenReturn(notification);
        when(em.merge(notification)).thenReturn(notification);
        
        // Act
        Notification result = notificationService.marquerCommeLue(notifId);
        
        // Assert
        assertTrue(result.getLue());
        assertNotNull(result.getDateLecture());
        verify(em, times(1)).merge(notification);
    }
    
    @Test
    public void testCountNonLues() {
        // Arrange
        Long userId = 1L;
        Long expectedCount = 5L;
        
        TypedQuery<Long> countQuery = mock(TypedQuery.class);
        when(em.createNamedQuery("Notification.countNonLues", Long.class))
            .thenReturn(countQuery);
        when(countQuery.setParameter("userId", userId)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(expectedCount);
        
        // Act
        Long result = notificationService.countNonLues(userId);
        
        // Assert
        assertEquals(expectedCount, result);
    }
}