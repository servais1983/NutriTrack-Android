# NutriTrack Web App

## Présentation

NutriTrack est une application web complète pour le suivi nutritionnel qui permet de :

- Calculer vos besoins caloriques personnalisés
- Suivre votre consommation alimentaire quotidienne
- Analyser votre progression sur le temps
- Gérer une base de données d'aliments personnalisée

Cette application a été convertie d'une application Android native en une application web fonctionnelle utilisant HTML, CSS et JavaScript.

## Fonctionnalités

- **Tableau de bord** : Suivi des calories et macronutriments quotidiens
- **Base de données d'aliments** : Ajout, recherche et gestion d'aliments
- **Scan de code-barres** : Utilisation de la caméra pour scanner des produits alimentaires
- **Suivi de progression** : Graphiques d'évolution du poids et des apports nutritionnels
- **Profil personnalisé** : Calcul des besoins énergétiques selon l'âge, le sexe, le poids et l'activité physique

## Technologies utilisées

- HTML5, CSS3 et JavaScript pour l'interface utilisateur
- LocalStorage pour la persistance des données
- Chart.js pour les graphiques de progression
- QuaggaJS pour le scan de code-barres
- GitHub Pages pour l'hébergement

## Activation de GitHub Pages

Pour rendre l'application accessible en ligne :

1. Accédez aux paramètres du dépôt en cliquant sur l'onglet "Settings" en haut de la page du dépôt
2. Dans la section "Code and automation" du menu de gauche, cliquez sur "Pages"
3. Sous "Source", sélectionnez "Deploy from a branch"
4. Sous "Branch", sélectionnez "gh-pages" et "/" (root), puis cliquez sur "Save"
5. Attendez quelques minutes pour que GitHub déploie le site
6. Une notification en haut de la page vous indiquera quand le site sera prêt et vous donnera l'URL

Une fois déployée, l'application sera accessible à l'adresse : `https://servais1983.github.io/NutriTrack-Android/`

## Comment utiliser cette application

### Accès en ligne
L'application est accessible directement à l'adresse : https://servais1983.github.io/NutriTrack-Android/

### Installation locale
1. Clonez ce dépôt : `git clone https://github.com/servais1983/NutriTrack-Android.git`
2. Naviguez vers la branche gh-pages : `git checkout gh-pages`
3. Ouvrez le fichier `index.html` dans votre navigateur

## Structure du projet

Le projet est organisé comme suit :

- `index.html` : Page principale de l'application
- `css/` : Feuilles de style CSS
  - `styles.css` : Styles principaux
  - `notifications.css` : Styles pour les notifications
- `js/` : Scripts JavaScript
  - `app.js` : Script principal et gestion des données
  - `navigation.js` : Gestion de la navigation entre les écrans
  - `dashboard.js` : Logique du tableau de bord
  - `food.js` : Gestion des aliments
  - `progress.js` : Graphiques de progression
  - `profile.js` : Gestion du profil utilisateur

## Fonctionnement

L'application utilise le stockage local (localStorage) de votre navigateur pour sauvegarder :

- Votre profil utilisateur et vos objectifs nutritionnels
- Votre base de données d'aliments
- Vos entrées alimentaires quotidiennes
- Vos entrées de poids

Aucune donnée n'est envoyée à un serveur externe. Tout reste stocké localement sur votre appareil.

## Limitations

- Le scan de code-barres nécessite un accès à la caméra et ne fonctionne que sur les navigateurs sécurisés (HTTPS)
- Les données sont stockées localement et peuvent être perdues si vous effacez les données de navigation
- La reconnaissance de codes-barres est simulée (dans une vraie application, un appel à une API de base de données alimentaires serait effectué)

## Améliorations futures

- Synchronisation avec une base de données en ligne
- Export et import des données
- Support multilingue
- Version responsive pour mobiles et tablettes
- Intégration avec des API d'aliments réelles

## Licence

Ce projet est sous licence MIT.