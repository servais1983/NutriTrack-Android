# NutriTrack - Application Web de Suivi Nutritionnel

## Conversion d'Android à Web

Ce projet a été converti d'une application Android native en une application web fonctionnelle et complète accessible depuis n'importe quel navigateur.

### Accès à l'application

**L'application est accessible en ligne à l'adresse :** [https://servais1983.github.io/NutriTrack-Android/](https://servais1983.github.io/NutriTrack-Android/)

## Fonctionnalités principales

### Profil personnalisé
- Configuration détaillée du profil (âge, sexe, poids, taille, niveau d'activité)
- Calcul automatique des besoins caloriques selon la formule de Mifflin-St Jeor
- Recommandations personnalisées de macronutriments selon l'objectif choisi

### Suivi nutritionnel
- Tableau de bord avec statistiques nutritionnelles en temps réel
- Tracking des calories et macronutriments quotidiens
- Organisation par type de repas (petit déjeuner, déjeuner, dîner, collation)
- Historique des aliments consommés

### Scan intelligent
- Scan de codes-barres pour identification rapide des produits (utilise la caméra)
- Simulation de reconnaissance de codes-barres
- Entrée rapide d'aliments dans le journal alimentaire

### Suivi de progression
- Graphique d'évolution du poids
- Suivi des tendances nutritionnelles sur la durée
- Visualisation des apports en macronutriments

## Technologies utilisées

- **Interface utilisateur** : HTML5, CSS3 et JavaScript natif (vanilla JS)
- **Stockage local** : LocalStorage pour la persistance des données
- **Scan de code-barres** : QuaggaJS pour l'accès à la caméra et la lecture de codes-barres
- **Visualisation de données** : Chart.js pour les graphiques interactifs
- **Déploiement** : GitHub Pages pour l'hébergement

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

## Branches du projet

- **main** : Branche principale avec les informations sur le projet
- **gh-pages** : Branche contenant l'application web déployée

## Installation locale

Pour utiliser l'application localement :

1. Clonez ce dépôt : `git clone https://github.com/servais1983/NutriTrack-Android.git`
2. Naviguez vers la branche gh-pages : `git checkout gh-pages`
3. Ouvrez le fichier `index.html` dans votre navigateur

## Limitations

- Le scan de code-barres nécessite un accès à la caméra et ne fonctionne que sur les navigateurs sécurisés (HTTPS)
- Les données sont stockées localement et peuvent être perdues si vous effacez les données de navigation
- La reconnaissance de codes-barres est simulée (dans une vraie application, un appel à une API de base de données alimentaires serait effectué)

## Améliorations futures

- Synchronisation avec une base de données en ligne
- Export et import des données
- Support multilingue
- Optimisation pour mobiles et tablettes
- Intégration avec des API d'aliments réelles

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou soumettre une Pull Request.

## Licence

Ce projet est sous licence MIT.