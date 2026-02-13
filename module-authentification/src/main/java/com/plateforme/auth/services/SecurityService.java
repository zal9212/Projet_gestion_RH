package com.plateforme.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.ejb.Stateless;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class SecurityService {

    // IMPORTANT : Dans un vrai projet, cette clé doit être stockée de manière sécurisée (config, variable d'env)
    private static final String SECRET = "votre_cle_secrete_tres_longue_et_tres_aleatoire_pour_le_jwt_solide_rh";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 24 heures en millisecondes

    /**
     * Hash un mot de passe en utilisant BCrypt.
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Vérifie si un mot de passe correspond au hash.
     */
    public boolean checkPassword(String password, String hashed) {
        try {
            return BCrypt.checkpw(password, hashed);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Génère un token JWT pour un utilisateur.
     */
    public String generateToken(String username, String role, Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", id);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valide et extrait les claims d'un token JWT.
     */
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
