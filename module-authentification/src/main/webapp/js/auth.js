/**
 * Gestion de l'authentification - Appels API et session.
 */
const AuthAPI = {

    /**
     * Connecte un utilisateur.
     */
    login: async function (username, password) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem(CONFIG.STORAGE_KEYS.TOKEN, data.token);

                // Récupérer le profil après login
                await this.loadProfile();
                return true;
            } else {
                const error = await response.text();
                throw new Error(error || "Identifiants incorrects");
            }
        } catch (error) {
            console.error('Erreur Login:', error);
            throw error;
        }
    },

    /**
     * Récupère le profil de l'utilisateur connecté.
     */
    loadProfile: async function () {
        try {
            const response = await SecurityInterceptor.fetch(`${CONFIG.API_BASE_URL}/auth/profile`);

            if (response.ok) {
                const user = await response.json();
                localStorage.setItem(CONFIG.STORAGE_KEYS.USER_ID, user.id);
                localStorage.setItem(CONFIG.STORAGE_KEYS.USER_NAME, user.nomUtilisateur);
                localStorage.setItem(CONFIG.STORAGE_KEYS.USER_ROLE, user.role);
                return user;
            }
        } catch (error) {
            console.error('Erreur Profil:', error);
        }
    },

    /**
     * Déconnecte l'utilisateur.
     */
    logout: function () {
        localStorage.removeItem(CONFIG.STORAGE_KEYS.TOKEN);
        localStorage.removeItem(CONFIG.STORAGE_KEYS.USER_ID);
        localStorage.removeItem(CONFIG.STORAGE_KEYS.USER_NAME);
        localStorage.removeItem(CONFIG.STORAGE_KEYS.USER_ROLE);
        window.location.href = 'login.html';
    }
};

/**
 * Redirection automatique si non connecté.
 */
if (window.location.pathname.indexOf('login.html') === -1) {
    if (!Utils.isAuthenticated()) {
        window.location.href = 'login.html';
    }
}
