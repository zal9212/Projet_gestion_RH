/**
 * Intercepteur de sécurité pour ajouter le token JWT aux requêtes.
 */
const SecurityInterceptor = {

    /**
     * Wrapper autour de fetch pour ajouter le header Authorization.
     */
    fetch: async function (url, options = {}) {
        const token = localStorage.getItem(CONFIG.STORAGE_KEYS.TOKEN);

        // Initialiser les headers si nécessaire
        if (!options.headers) {
            options.headers = {};
        }

        // Ajouter le token si présent
        if (token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }

        // Si c'est un POST/PUT sans Content-Type, on met JSON par défaut
        if ((options.method === 'POST' || options.method === 'PUT') && !options.headers['Content-Type']) {
            options.headers['Content-Type'] = 'application/json';
        }

        try {
            const response = await fetch(url, options);

            // Gérer l'expiration du token ou accès refusé
            if (response.status === 401) {
                console.warn("Session expirée ou non autorisée");
                localStorage.removeItem(CONFIG.STORAGE_KEYS.TOKEN);
                window.location.href = 'login.html';
            }

            return response;
        } catch (error) {
            console.error('Erreur Réseau:', error);
            throw error;
        }
    }
};
