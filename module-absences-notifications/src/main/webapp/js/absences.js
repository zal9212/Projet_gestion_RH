/**
 * Gestion des absences - Appels API et logique métier.
 */
const AbsenceAPI = {
    
    /**
     * Récupère toutes les absences.
     */
    getAll: async function() {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences`);
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur lors de la récupération des absences:', error);
            throw error;
        }
    },
    
    /**
     * Récupère les absences d'un employé.
     */
    getByEmploye: async function(employeId) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences/employe/${employeId}`);
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
     * Récupère les absences sur une période.
     */
    getByPeriode: async function(debut, fin) {
        try {
            const response = await fetch(
                `${CONFIG.API_BASE_URL}/absences/periode?debut=${debut}&fin=${fin}`
            );
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
     * Récupère les absences non justifiées.
     */
    getNonJustifiees: async function() {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences/non-justifiees`);
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
     * Crée une nouvelle absence.
     */
    create: async function(absenceData) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(absenceData)
            });
            
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur lors de la création de l\'absence:', error);
            throw error;
        }
    },
    
    /**
     * Met à jour une absence.
     */
    update: async function(id, absenceData) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(absenceData)
            });
            
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur lors de la mise à jour:', error);
            throw error;
        }
    },
    
    /**
     * Justifie une absence.
     */
    justifier: async function(id, documentPath) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences/${id}/justifier`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ documentPath })
            });
            
            const data = await response.json();
            
            if (data.success) {
                return data.data;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur lors de la justification:', error);
            throw error;
        }
    },
    
    /**
     * Supprime une absence.
     */
    delete: async function(id) {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences/${id}`, {
                method: 'DELETE'
            });
            
            const data = await response.json();
            
            if (data.success) {
                return true;
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error('Erreur lors de la suppression:', error);
            throw error;
        }
    }
};

/**
 * Gestion des types d'absences.
 */
const TypeAbsenceAPI = {
    
    /**
     * Récupère tous les types d'absences.
     */
    getAll: async function() {
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}/absences/types`);
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