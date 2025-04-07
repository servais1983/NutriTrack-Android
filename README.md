# NutriTrack Android

Application Android complète pour calculer les besoins caloriques personnalisés, scanner les étiquettes nutritionnelles et suivre l'évolution de la condition physique.

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
- Scan de codes-barres pour identification rapide des produits
- Reconnaissance optique des étiquettes nutritionnelles
- Extraction automatique des valeurs nutritionnelles

### Suivi de progression
- Graphique d'évolution du poids
- Suivi des tendances sur la durée
- Enregistrement d'historique

## Technologies utilisées

- **Architecture** : MVVM (Model-View-ViewModel) avec LiveData et Repository
- **Stockage local** : Room Database pour une persistance des données optimisée
- **Interface utilisateur** : Material Design Components pour une expérience moderne
- **Capture d'image** : CameraX pour l'intégration de la caméra
- **Vision par ordinateur** : ML Kit pour la reconnaissance de texte et codes-barres
- **Visualisation de données** : MPAndroidChart pour les graphiques interactifs

## Structure du projet

- **database** : Entités, DAOs et classes d'utilitaires pour la persistance des données
- **repository** : Couche d'accès aux données avec méthodes CRUD
- **viewmodel** : Logique de présentation et gestion des états
- **dialog** : Boîtes de dialogue pour l'interaction utilisateur
- **adapter** : Adaptateurs pour les listes RecyclerView
- **barcode** / **nutrition** : Modules pour la reconnaissance optique


## Améliorations futures

- Synchronisation avec des bases de données nutritionnelles en ligne
- Reconnaissance d'aliments par photo
- Suggestions personnalisées de repas
- Intégration avec Google Fit et autres wearables
- Mode hors-ligne amélioré
- Support multilingue complet

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou soumettre une Pull Request.

## Licence

Ce projet est sous licence MIT.
