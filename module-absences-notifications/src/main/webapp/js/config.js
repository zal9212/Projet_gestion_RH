/**
 * Configuration globale de l'application.
 */
const CONFIG = {
    // URL de base de l'API
    API_BASE_URL: 'http://localhost:8080/absences-notifications/api',

    // Clés de stockage local
    STORAGE_KEYS: {
        USER_ID: 'userId',
        USER_NAME: 'userName',
        USER_ROLE: 'userRole',
        TOKEN: 'authToken'
    },

    // Types de notifications
    NOTIFICATION_TYPES: {
        ABSENCE: 'ABSENCE',
        CONGE_APPROUVE: 'CONGE_APPROUVE',
        CONGE_REJETE: 'CONGE_REJETE',
        DEMANDE_CONGE: 'DEMANDE_CONGE',
        INFO: 'INFO'
    },

    // Rôles utilisateurs
    ROLES: {
        ADMIN: 'ADMIN',
        MANAGER: 'MANAGER',
        EMPLOYE: 'EMPLOYE'
    }
};

/**
 * Utilitaires généraux.
 */
const Utils = {

    /**
     * Formate une date au format français.
     */
    formatDate: function (dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    },

    /**
     * Formate une date et heure.
     */
    formatDateTime: function (dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleString('fr-FR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    /**
     * Affiche un message de succès.
     */
    showSuccess: function (message) {
        this.showAlert(message, 'success');
    },

    /**
     * Affiche un message d'erreur.
     */
    showError: function (message) {
        this.showAlert(message, 'error');
    },

    /**
     * Affiche une alerte.
     */
    showAlert: function (message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;

        // Ajouter au body
        document.body.appendChild(alertDiv);

        // Retirer après 5 secondes avec un fondu de sortie
        setTimeout(() => {
            alertDiv.style.opacity = '0';
            alertDiv.style.transform = 'translateY(100px)';
            alertDiv.style.transition = '0.3s ease-in';
            setTimeout(() => alertDiv.remove(), 300);
        }, 5000);
    },

    /**
     * Récupère l'utilisateur connecté depuis le localStorage.
     */
    getCurrentUser: function () {
        return {
            id: localStorage.getItem(CONFIG.STORAGE_KEYS.USER_ID),
            name: localStorage.getItem(CONFIG.STORAGE_KEYS.USER_NAME),
            role: localStorage.getItem(CONFIG.STORAGE_KEYS.USER_ROLE)
        };
    },

    /**
     * Vérifie si l'utilisateur est connecté.
     */
    isAuthenticated: function () {
        const userId = localStorage.getItem(CONFIG.STORAGE_KEYS.USER_ID);
        return userId !== null && userId !== undefined;
    },

    /**
     * Vérifie si l'utilisateur a un rôle spécifique.
     */
    checkRole: function (allowedRoles) {
        const user = this.getCurrentUser();
        if (!user || !user.role) return false;

        if (Array.isArray(allowedRoles)) {
            return allowedRoles.includes(user.role);
        }
        return user.role === allowedRoles;
    },

    /**
     * Redirige l'utilisateur s'il n'a pas le rôle requis.
     */
    requireRole: function (allowedRoles, redirectUrl = 'index.html') {
        if (!this.checkRole(allowedRoles)) {
            this.showError("Accès refusé : vous n'avez pas les droits nécessaires.");
            setTimeout(() => window.location.href = redirectUrl, 1500);
            return false;
        }
        return true;
    },

    /**
     * Masque les éléments basés sur les rôles (attribut data-role).
     */
    applyRoleSecurity: function () {
        const user = this.getCurrentUser();
        const role = user ? user.role : null;

        document.querySelectorAll('[data-role]').forEach(el => {
            const allowedRolesStr = el.getAttribute('data-role');
            if (!allowedRolesStr) return;

            const allowedRoles = allowedRolesStr.split(',');
            if (!role || !allowedRoles.includes(role)) {
                el.style.display = 'none';
            }
        });
    },

    /**
     * Redirige vers la page de connexion si non authentifié.
     */
    requireAuth: function () {
        if (!this.isAuthenticated()) {
            window.location.href = '/login.html';
        }
    }
};

// Simuler une connexion pour les tests (à retirer en production)
if (!Utils.isAuthenticated()) {
    localStorage.setItem(CONFIG.STORAGE_KEYS.USER_ID, '4');
    localStorage.setItem(CONFIG.STORAGE_KEYS.USER_NAME, 'Aminata Sow');
    localStorage.setItem(CONFIG.STORAGE_KEYS.USER_ROLE, CONFIG.ROLES.EMPLOYE);
}