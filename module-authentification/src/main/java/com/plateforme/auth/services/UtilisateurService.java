package com.plateforme.auth.services;

import com.plateforme.auth.entities.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UtilisateurService {

    @PersistenceContext(unitName = "authPU")
    private EntityManager em;

    @EJB
    private SecurityService securityService;

    /**
     * Crée un nouvel utilisateur en hachant son mot de passe.
     */
    public Utilisateur create(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(securityService.hashPassword(utilisateur.getMotDePasse()));
        em.persist(utilisateur);
        return utilisateur;
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    public Utilisateur findById(Long id) {
        return em.find(Utilisateur.class, id);
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur.
     */
    public Utilisateur findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.nomUtilisateur = :username", Utilisateur.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Liste tous les utilisateurs.
     */
    public List<Utilisateur> findAll() {
        return em.createQuery("SELECT u FROM Utilisateur u", Utilisateur.class).getResultList();
    }

    /**
     * Met à jour un utilisateur.
     */
    public Utilisateur update(Utilisateur utilisateur) {
        return em.merge(utilisateur);
    }

    /**
     * Change le mot de passe d'un utilisateur.
     */
    public boolean changePassword(Long id, String newPassword) {
        Utilisateur u = findById(id);
        if (u != null) {
            u.setMotDePasse(securityService.hashPassword(newPassword));
            em.merge(u);
            return true;
        }
        return false;
    }

    /**
     * Supprime un utilisateur (ou le désactive).
     */
    public void delete(Long id) {
        Utilisateur u = findById(id);
        if (u != null) {
            em.remove(u);
        }
    }
}
