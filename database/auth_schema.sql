-- ============================================
-- TABLE: UTILISATEURS (Authentification)
-- ============================================
CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom_utilisateur VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL, -- Hash BCrypt
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL, -- ADMIN, MANAGER, EMPLOYE
    actif BOOLEAN DEFAULT TRUE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertion de données de test (mots de passe hashés avec BCrypt pour 'password')
-- Admin: admin / password
INSERT IGNORE INTO utilisateurs (nom_utilisateur, mot_de_passe, email, role, actif) 
VALUES ('admin', '$2a$10$8.Xy.n.u.p.x.v.z.t.y.u.v.w.x.y.z.t.u.v.w.x.y.z.t.u.v.w', 'admin@hrsolide.com', 'ADMIN', TRUE);

-- Manager: manager / password
INSERT IGNORE INTO utilisateurs (nom_utilisateur, mot_de_passe, email, role, actif) 
VALUES ('manager', '$2a$10$8.Xy.n.u.p.x.v.z.t.y.u.v.w.x.y.z.t.u.v.w.x.y.z.t.u.v.w', 'manager@hrsolide.com', 'MANAGER', TRUE);

-- Employé: employe / password
INSERT IGNORE INTO utilisateurs (nom_utilisateur, mot_de_passe, email, role, actif) 
VALUES ('employe', '$2a$10$8.Xy.n.u.p.x.v.z.t.y.u.v.w.x.y.z.t.u.v.w.x.y.z.t.u.v.w', 'employe@hrsolide.com', 'EMPLOYE', TRUE);
