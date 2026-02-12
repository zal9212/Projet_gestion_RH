# 🚀 Guide d'Exécution sur Eclipse

Pour exécuter ce projet multi-module dans Eclipse, suivez ces étapes :

## 1. Importation du Projet
1. Ouvrez Eclipse IDE (version **Eclipse IDE for Enterprise Java and Web Developers** recommandée).
2. Faites `File` > `Import...`.
3. Choisissez `Maven` > `Existing Maven Projects`.
4. Sélectionnez le dossier racine `Projet JEE`.
5. Assurez-vous que tous les modules (`common`, `module-absences-notifications`, etc.) sont cochés et cliquez sur `Finish`.

## 2. Configuration du Serveur WildFly
1. Allez dans la vue **Servers** (si non visible : `Window` > `Show View` > `Servers`).
2. Faites un clic droit > `New` > `Server`.
3. Sélectionnez `Red Hat JBoss Middleware` > `JBoss Community` > **WildFly 39** (ou la version correspondante).
   > [!NOTE]
   > Si WildFly n'apparaît pas, installez les "JBoss Tools" via le Eclipse Marketplace.
4. Cliquez sur `Next` et pointez vers le répertoire d'installation : `C:\wildfly\wildfly-39.0.0.Final`.
5. Choisissez l'exécution avec le profil `standalone-full.xml`.

## 3. Ajout des Projets au Serveur
1. Dans la vue **Servers**, faites un clic droit sur votre serveur WildFly > `Add and Remove...`.
2. Ajoutez les modules que vous souhaitez tester (ex: `module-absences-notifications`).
3. Cliquez sur `Finish`.

## 4. Configuration de la Base de Données (XAMPP)
Avant de démarrer le serveur, assurez-vous que MySQL est actif :
1. Lancez **XAMPP Control Panel**.
2. Démarrez le module **MySQL**.

## 5. Lancement
1. Dans la vue **Servers**, cliquez sur l'icône **Play** (Start) ou faites un clic droit > `Start`.
2. L'application sera accessible sur : `http://localhost:8080/absences-notifications/`

---

### Tips
- Si vous avez des erreurs de compilation, faites un clic droit sur le projet racine > `Maven` > `Update Project...` > cochez `Force Update of Snapshots/Releases`.
- Assurez-vous d'utiliser un **JDK 21** dans Eclipse (`Window` > `Preferences` > `Java` > `Installed JREs`).
