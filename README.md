# NutriTrack - Application Web de Suivi Nutritionnel

NutriTrack est une application web permettant de calculer les besoins caloriques, suivre les apports nutritionnels et surveiller l'évolution de la condition physique.

## Fonctionnalités

- **Authentification sécurisée**: Création de compte et connexion avec email et mot de passe
- **Tableau de bord personnalisé**: Aperçu des statistiques nutritionnelles journalières
- **Journal alimentaire**: Suivi détaillé des repas et aliments consommés
- **Calcul des besoins caloriques**: Basé sur les caractéristiques personnelles (âge, poids, taille, niveau d'activité)
- **Suivi de progression**: Graphiques d'évolution du poids et des apports nutritionnels
- **Profil personnalisable**: Paramètres adaptés aux objectifs de chacun

## Technologies utilisées

- HTML5, CSS3, JavaScript (ES6+)
- Stockage local (localStorage) pour la persistance des données
- Chart.js pour les visualisations graphiques

## Installation

1. Clonez le dépôt
```bash
git clone https://github.com/servais1983/NutriTrack-WebAppli.git
```

2. Ouvrez le fichier `index.html` dans votre navigateur

Aucun serveur n'est nécessaire pour le fonctionnement de base car toutes les données sont stockées localement.

## Structure du projet

- `index.html` - Page principale de l'application
- `css/` - Feuilles de style
- `js/` - Scripts JavaScript
  - `auth.js` - Gestion de l'authentification
  - `storage.js` - Gestion du stockage des données
  - `app.js` - Logique principale de l'application

## Fonctionnement de la persistance des données

L'application utilise le stockage local du navigateur (localStorage) pour conserver :
- Les informations des utilisateurs (profils)
- Les entrées alimentaires
- L'historique de poids
- Les préférences utilisateur

Cela permet d'avoir une expérience complète sans nécessiter de connexion à un serveur.

## Notes de développement

Cette application était initialement une application Android qui a été convertie en application web. Les fonctionnalités principales ont été conservées, mais l'interface a été adaptée pour le web.
