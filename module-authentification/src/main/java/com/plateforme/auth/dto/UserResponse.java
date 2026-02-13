package com.plateforme.auth.dto;

import com.plateforme.auth.entities.Utilisateur;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UserResponse implements Serializable {
    private Long id;
    private String nomUtilisateur;
    private String email;
    private String role;
    private boolean actif;
    private LocalDateTime dateCreation;

    public UserResponse() {}

    public UserResponse(Utilisateur u) {
        this.id = u.getId();
        this.nomUtilisateur = u.getNomUtilisateur();
        this.email = u.getEmail();
        this.role = u.getRole();
        this.actif = u.isActif();
        this.dateCreation = u.getDateCreation();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
