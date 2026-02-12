/**
 * Gestion des notifications - Appels API et logique métier.
 */
const NotificationAPI = {
    
    /**
     * Récupère les notifications d'un utilisateur.
     */
    getByUser: async function(userId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/notifications?userId=${userId}`);
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },
    
    /**
     * Récupère les notifications non lues.
     */
    getNonLues: async function(userId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/notifications/non-lues?userId=${userId}`);
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },
    
    /**
     * Compte les notifications non lues.
     */
    countNonLues: async function(userId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/notifications/count-non-lues?userId=${userId}`);
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },
    
    /**
     * Marque une notification comme lue.
     */
    marquerCommeLue: async function(notificationId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/notifications/${notificationId}/lire`, {
                method: 'PUT'
            });
            
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },
    
    /**
     * Marque toutes les notifications comme lues.
     */
    marquerToutesCommeLues: async function(userId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/notifications/tout-lire?userId=${userId}`, {
                method: 'PUT'
            });
            
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },
    
    /**
     * Supprime une notification.
     */
    delete: async function(notificationId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/notifications/${notificationId}`, {
                method: 'DELETE'
            });
            
            const data = await response.json();
            
            if (data.success) {
                return true;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },
    
    /**
     * Récupère les statistiques.
     */
    getStatistiques: async function(userId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/notifications/statistiques?userId=${userId}`);
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    }
};

/**
 * Gestionnaire de badge de notifications.
 */
const NotificationBadge = {
    
    /**
     * Met à jour le badge avec le nombre de notifications non lues.
     */
    update: async function() {
        const user = Utils.getCurrentUser();
        
        try {
            const count = await NotificationAPI.countNonLues(user.id);
            
            const badge = document.getElementById('notification-badge');
            if (badge) {
                if (count > 0) {
                    badge.textContent = count > 99 ? '99+' : count;
                    badge.style.display = 'inline-block';
                } else {
                    badge.style.display = 'none';
                }
            }
        } catch (error) {
            console.error('Erreur lors de la mise à jour du badge:', error);
        }
    },
    
    /**
     * Initialise la mise à jour automatique du badge.
     */
    init: function() {
        // Mise à jour initiale
        this.update();
        
        // Mise à jour toutes les 30 secondes
        setInterval(() => {
            this.update();
        }, 30000);
    }
};