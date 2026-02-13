package com.plateforme.auth.services;

import com.plateforme.auth.entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class AuthentificationService {

    @EJB
    private UtilisateurService utilisateurService;

    @EJB
    private SecurityService securityService;

    /**
     * Authentifie un utilisateur et retourne un token JWT si réussi.
     */
    public String login(String username, String password) {
        Utilisateur u = utilisateurService.findByUsername(username);
        
        if (u != null && u.isActif() && securityService.checkPassword(password, u.getMotDePasse())) {
            return securityService.generateToken(u.getNomUtilisateur(), u.getRole(), u.getId());
        }
        
        return null;
    }
}
