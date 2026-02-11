# 🚀 Plateforme de Gestion RH - Java EE 10

Bienvenue sur le projet de plateforme de gestion des ressources humaines. Ce projet est structuré de manière multi-module pour permettre à une équipe de 4 développeurs de travailler en parallèle de façon autonome et efficace.

---

## 🏛️ Architecture du Projet

Le projet utilise une architecture **Multi-Module Maven**. Chaque développeur est responsable d'un module complet (Backend + Frontend).

```text
plateforme-rh/
├── common/                          # Classes partagées (DTOs, Utils, Events JMS)
├── module-authentification/         # [👤 PERSONNE 1] Auth & Gestion Utilisateurs
├── module-employes/                # [👤 PERSONNE 2] Gestion des Employés
├── module-conges/                  # [👤 PERSONNE 3] Gestion des Congés
└── module-absences-notifications/   # [👤 PERSONNE 4] Absences & JMS Notifications
```

---

## 🛠️ Stack Technique

- **Java Version** : 21
- **Plateforme** : Jakarta EE 10 (Migration effectuée depuis javax.*)
- **Serveur d'application** : WildFly 39.0.0.Final (Profil `standalone-full.xml`)
- **Base de données** : MySQL / MariaDB
- **Messaging** : JMS (ActiveMQ Artemis intégré à WildFly)

---

## 🚀 Guide de Démarrage Rapide

### 1. Prérequis
- **JDK 21** configuré.
- **Maven 3.9+** installé.
- **WildFly 39.0.0.Final** téléchargé et extrait.

### 2. Configuration de WildFly
Le projet nécessite une DataSource et une Queue JMS.
1. Démarrez WildFly avec le profil complet :
   ```powershell
   .\bin\standalone.bat -c standalone-full.xml
   ```
2. Utilisez le script de configuration fourni (via `jboss-cli`) pour créer :
   - La DataSource : `java:/gestion_rh_jeeDS`
   - La File d'attente : `java:/jms/queue/NotificationQueue`

### 3. Compilation et Installation
À la racine du projet, lancez la commande suivante pour tout compiler :
```bash
mvn clean install
```
Cette commande générera les fichiers `.war` dans le dossier `target/` de chaque module.

---

## 🤝 Consignes pour les Collaborateurs

### ⚠️ IMPORTANT : Namespace Jakarta
WildFly 39 n'accepte plus les imports `javax.*`. Vous **devez** utiliser les imports `jakarta.*` (ex: `jakarta.persistence.*`, `jakarta.ws.rs.*`).

### 📂 Où travailler ?
- Ne modifiez **QUE** les fichiers dans votre dossier de module.
- Si vous avez besoin d'une classe utilitaire partagée, ajoutez-la dans le module `common`.
- **Base de données** : Le fichier `persistence.xml` est configuré pour utiliser la source de données commune. Veillez à ce que vos noms de tables ne créent pas de conflits.

---

## 📬 Intégration JMS (Exemple)
Pour envoyer une notification asynchrone depuis votre module (ex: Congés) vers le module Notifications :
1. Injectez le `NotificationProducer` (ou envoyez manuellement un `NotificationEvent`).
2. Le message sera traité automatiquement par le MDB du module `absences-notifications`.

---

## 📄 Documentation API
Chaque module expose ses API sous le préfixe `/api`.
Exemple : `http://localhost:8080/module-authentification/api/...`

---
*Développé avec ❤️ par l'équipe RH.*
