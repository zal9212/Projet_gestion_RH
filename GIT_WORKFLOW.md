# 🌿 Guide Git & GitHub - Stratégie de Branches

Ce guide explique comment organiser le travail collaboratif sur GitHub pour les 4 membres de l'équipe.

---

## 📋 Stratégie de Branches

### Structure des Branches

```text
main (production)
  ├── develop (intégration)
  │   ├── feature/auth-module (Personne 1)
  │   ├── feature/employes-module (Personne 2)
  │   ├── feature/conges-module (Personne 3)
  │   └── feature/absences-module (Personne 4)
```

---

## 🚀 Configuration Initiale (Une seule fois)

### 1. Initialiser le dépôt Git (Personne 4 - Vous)

```powershell
# Dans le dossier racine du projet
cd "c:\Users\USER\Desktop\Cours LTI3-DAR\Cours java EE\Projet-groupe-gestion-rh\Projet JEE"

# Initialiser Git
git init

# Créer le fichier .gitignore
# (voir section suivante)

# Ajouter tous les fichiers
git add .

# Premier commit
git commit -m "Initial commit: Structure multi-module Jakarta EE 10"

# Créer la branche develop
git branch develop
git checkout develop

# Créer le dépôt sur GitHub et le lier
git remote add origin https://github.com/VOTRE-USERNAME/plateforme-rh.git

# Pousser les branches main et develop
git checkout main
git push -u origin main
git checkout develop
git push -u origin develop
```

### 2. Créer le fichier `.gitignore`

```gitignore
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties

# IDE
.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# Logs
*.log

# OS
.DS_Store
Thumbs.db

# WildFly
wildfly/
*.war
*.ear
*.jar

# Temporaires
*.tmp
*.bak
*.swp
*~
```

---

## 👥 Workflow pour Chaque Membre

### Étape 1 : Cloner le Projet (Première fois)

```powershell
# Cloner le dépôt
git clone https://github.com/VOTRE-USERNAME/plateforme-rh.git
cd plateforme-rh

# Se placer sur develop
git checkout develop
```

### Étape 2 : Créer sa Branche de Fonctionnalité

Chaque membre crée **SA** branche depuis `develop` :

**Personne 1 (Authentification)** :
```powershell
git checkout develop
git pull origin develop
git checkout -b feature/auth-module
git push -u origin feature/auth-module
```

**Personne 2 (Employés)** :
```powershell
git checkout develop
git pull origin develop
git checkout -b feature/employes-module
git push -u origin feature/employes-module
```

**Personne 3 (Congés)** :
```powershell
git checkout develop
git pull origin develop
git checkout -b feature/conges-module
git push -u origin feature/conges-module
```

**Personne 4 (Absences - Vous)** :
```powershell
git checkout develop
git pull origin develop
git checkout -b feature/absences-module
git push -u origin feature/absences-module
```

### Étape 3 : Travailler sur sa Branche

```powershell
# Vérifier qu'on est sur la bonne branche
git branch

# Travailler normalement dans son module
# Par exemple, Personne 4 travaille uniquement dans module-absences-notifications/

# Ajouter les modifications
git add module-absences-notifications/

# Commit réguliers (plusieurs fois par jour)
git commit -m "feat: Ajout de l'API REST pour les absences"

# Pousser vers GitHub
git push origin feature/absences-module
```

### Étape 4 : Synchroniser avec `develop` (Quotidien)

Pour récupérer les changements des autres membres :

```powershell
# Sauvegarder son travail en cours
git add .
git commit -m "WIP: Travail en cours"

# Récupérer les mises à jour de develop
git checkout develop
git pull origin develop

# Retourner sur sa branche
git checkout feature/absences-module

# Fusionner develop dans sa branche
git merge develop

# Résoudre les conflits si nécessaire
# Puis pousser
git push origin feature/absences-module
```

---

## 🔄 Intégration : Pull Request (PR)

Quand un module est terminé et testé :

### 1. Créer une Pull Request sur GitHub
1. Aller sur GitHub → Onglet "Pull Requests"
2. Cliquer sur "New Pull Request"
3. Base : `develop` ← Compare : `feature/votre-module`
4. Titre : `[Module Absences] Implémentation complète`
5. Description détaillée des changements

### 2. Code Review
- Les autres membres reviewent le code
- Discutent des changements
- Approuvent ou demandent des modifications

### 3. Merge dans `develop`
Une fois approuvé, le responsable merge la PR dans `develop`.

---

## 🎯 Règles d'Or

### ✅ À FAIRE
- **Travailler UNIQUEMENT dans son dossier de module**
- Faire des commits réguliers avec des messages clairs
- Synchroniser avec `develop` **tous les jours**
- Tester son module avant de créer une PR
- Ne jamais pusher directement sur `main` ou `develop`

### ❌ À ÉVITER
- Modifier les fichiers des autres modules
- Faire des commits géants (préférer plusieurs petits commits)
- Oublier de pull avant de push
- Commiter des fichiers `target/` ou `.class`

---

## 📝 Convention de Nommage des Commits

Utilisez des préfixes clairs :

```text
feat: Nouvelle fonctionnalité
fix: Correction de bug
refactor: Refactorisation du code
docs: Documentation
test: Ajout de tests
chore: Tâches de maintenance
```

**Exemples** :
```bash
git commit -m "feat: Ajout de l'API POST /api/absences"
git commit -m "fix: Correction du bug de validation des dates"
git commit -m "docs: Mise à jour du README avec les endpoints"
```

---

## 🆘 Commandes Utiles

```powershell
# Voir l'état des fichiers
git status

# Voir l'historique
git log --oneline --graph

# Annuler les modifications non commitées
git checkout -- fichier.java

# Voir les différences
git diff

# Changer de branche
git checkout nom-de-branche

# Voir toutes les branches
git branch -a
```

---

## 🎓 Exemple Complet : Journée Type de Personne 4

```powershell
# Matin : Synchronisation
git checkout feature/absences-module
git pull origin feature/absences-module
git checkout develop
git pull origin develop
git checkout feature/absences-module
git merge develop

# Travail : Développement
# ... coder dans module-absences-notifications/ ...

# Midi : Sauvegarde
git add module-absences-notifications/
git commit -m "feat: Implémentation du NotificationMDB"
git push origin feature/absences-module

# Après-midi : Suite du travail
# ... continuer à coder ...

# Soir : Sauvegarde finale
git add .
git commit -m "feat: Ajout des tests unitaires pour AbsenceService"
git push origin feature/absences-module
```

---

**Bon courage à toute l'équipe ! 🚀**
