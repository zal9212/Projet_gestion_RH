package com.plateforme.absences.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuration de l'application REST.
 * 
 * Toutes les ressources REST seront accessibles sous /api/*
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    // La classe peut rester vide
    // JAX-RS détecte automatiquement toutes les classes @Path
}
