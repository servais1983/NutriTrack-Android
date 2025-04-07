// Variables globales
let currentUser = null;
let foodDatabase = [];
let mealEntries = [];
let weightEntries = [];

// Initialisation de l'application
document.addEventListener('DOMContentLoaded', function() {
    // Charger les données depuis le localStorage
    loadData();
    
    // Initialiser le profil utilisateur si nécessaire
    if (!currentUser) {
        initDefaultUser();
    }
    
    // Mettre à jour l'interface utilisateur avec les données chargées
    updateUI();
    
    // Ajouter les écouteurs d'événements pour les formulaires
    setupEventListeners();
});

// Fonction pour charger les données depuis le localStorage
function loadData() {
    try {
        // Charger l'utilisateur
        const storedUser = localStorage.getItem('nutritrack_user');
        if (storedUser) {
            currentUser = JSON.parse(storedUser);
        }
        
        // Charger la base de données d'aliments
        const storedFoods = localStorage.getItem('nutritrack_foods');
        if (storedFoods) {
            foodDatabase = JSON.parse(storedFoods);
        } else {
            // Charger les aliments par défaut si aucun n'est stocké
            loadDefaultFoods();
        }
        
        // Charger les entrées de repas
        const storedMeals = localStorage.getItem('nutritrack_meals');
        if (storedMeals) {
            mealEntries = JSON.parse(storedMeals);
        }
        
        // Charger les entrées de poids
        const storedWeights = localStorage.getItem('nutritrack_weights');
        if (storedWeights) {
            weightEntries = JSON.parse(storedWeights);
        }
        
        console.log('Données chargées avec succès');
    } catch (error) {
        console.error('Erreur lors du chargement des données:', error);
        // En cas d'erreur, initialiser avec des données par défaut
        initDefaultUser();
        loadDefaultFoods();
    }
}

// Fonction pour sauvegarder les données dans le localStorage
function saveData() {
    try {
        localStorage.setItem('nutritrack_user', JSON.stringify(currentUser));
        localStorage.setItem('nutritrack_foods', JSON.stringify(foodDatabase));
        localStorage.setItem('nutritrack_meals', JSON.stringify(mealEntries));
        localStorage.setItem('nutritrack_weights', JSON.stringify(weightEntries));
        console.log('Données sauvegardées avec succès');
    } catch (error) {
        console.error('Erreur lors de la sauvegarde des données:', error);
    }
}

// Fonction pour initialiser un utilisateur par défaut
function initDefaultUser() {
    currentUser = {
        name: 'Utilisateur',
        email: '',
        birthdate: '',
        gender: '',
        height: 170, // cm
        weight: 70, // kg
        activityLevel: 1.375, // Légèrement actif
        goal: 'maintain', // Maintien du poids
        // Besoins nutritionnels par défaut
        nutritionGoals: {
            calories: 2000,
            protein: 150,
            carbs: 250,
            fat: 65
        }
    };
    
    // Ajouter une entrée de poids initiale
    const today = new Date();
    weightEntries = [{
        id: generateId(),
        date: today.toISOString().split('T')[0],
        weight: 70,
        notes: 'Poids initial'
    }];
    
    saveData();
}

// Fonction pour charger des aliments par défaut
function loadDefaultFoods() {
    foodDatabase = [
        {
            id: 'food1',
            name: 'Poulet grillé',
            calories: 165,
            protein: 31,
            carbs: 0,
            fat: 3.6,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food2',
            name: 'Riz blanc cuit',
            calories: 130,
            protein: 2.7,
            carbs: 28,
            fat: 0.3,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food3',
            name: 'Brocoli cuit',
            calories: 55,
            protein: 3.7,
            carbs: 11.2,
            fat: 0.6,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food4',
            name: 'Œuf entier',
            calories: 72,
            protein: 6.3,
            carbs: 0.4,
            fat: 5,
            servingSize: 50,
            favorite: false,
            custom: false
        },
        {
            id: 'food5',
            name: 'Banane',
            calories: 89,
            protein: 1.1,
            carbs: 22.8,
            fat: 0.3,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food6',
            name: 'Pain complet',
            calories: 247,
            protein: 10.9,
            carbs: 41.3,
            fat: 4.2,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food7',
            name: 'Yaourt nature',
            calories: 59,
            protein: 3.5,
            carbs: 4.7,
            fat: 3.2,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food8',
            name: 'Saumon',
            calories: 206,
            protein: 22.1,
            carbs: 0,
            fat: 12.4,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food9',
            name: 'Pomme',
            calories: 52,
            protein: 0.3,
            carbs: 13.8,
            fat: 0.2,
            servingSize: 100,
            favorite: false,
            custom: false
        },
        {
            id: 'food10',
            name: 'Amandes',
            calories: 579,
            protein: 21.2,
            carbs: 21.7,
            fat: 49.9,
            servingSize: 100,
            favorite: false,
            custom: false
        }
    ];
    
    saveData();
}

// Fonction pour mettre à jour l'interface utilisateur
function updateUI() {
    // Mettre à jour les informations du profil
    updateProfileUI();
    
    // Mettre à jour le tableau de bord
    updateDashboardUI();
    
    // Mettre à jour la liste d'aliments
    updateFoodListUI();
    
    // Mettre à jour les graphiques de progression
    updateProgressCharts();
}

// Fonction pour configurer les écouteurs d'événements
function setupEventListeners() {
    // Formulaire de profil
    const profileForm = document.getElementById('profileForm');
    if (profileForm) {
        profileForm.addEventListener('submit', handleProfileSubmit);
    }
    
    // Formulaire d'ajout d'aliment personnalisé
    const customFoodForm = document.getElementById('customFoodForm');
    if (customFoodForm) {
        customFoodForm.addEventListener('submit', handleCustomFoodSubmit);
    }
    
    // Formulaire d'ajout de poids
    const weightForm = document.getElementById('weightForm');
    if (weightForm) {
        weightForm.addEventListener('submit', handleWeightSubmit);
    }
    
    // Formulaire d'ajout d'aliment au journal
    const addFoodToLogForm = document.getElementById('addFoodToLogForm');
    if (addFoodToLogForm) {
        addFoodToLogForm.addEventListener('submit', handleAddFoodToLog);
    }
    
    // Écouteurs pour les filtres d'aliments
    const filterButtons = document.querySelectorAll('.filter-btn');
    if (filterButtons.length) {
        filterButtons.forEach(button => {
            button.addEventListener('click', handleFoodFilter);
        });
    }
    
    // Écouteurs pour les onglets de progression
    const progressTabButtons = document.querySelectorAll('.progress-tab-btn');
    if (progressTabButtons.length) {
        progressTabButtons.forEach(button => {
            button.addEventListener('click', handleProgressTab);
        });
    }
    
    // Écouteurs pour les boutons de période
    const periodButtons = document.querySelectorAll('.period-btn');
    if (periodButtons.length) {
        periodButtons.forEach(button => {
            button.addEventListener('click', handlePeriodSelect);
        });
    }
}

// Fonction utilitaire pour générer un ID unique
function generateId() {
    return '_' + Math.random().toString(36).substr(2, 9);
}

// Fonction pour afficher une notification
function showNotification(message, type = 'success') {
    // Créer un élément de notification
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    // Ajouter au corps du document
    document.body.appendChild(notification);
    
    // Afficher avec une animation
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);
    
    // Retirer après un délai
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 3000);
}

// Fonction pour formater une date
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Fonction pour obtenir la date actuelle au format YYYY-MM-DD
function getCurrentDate() {
    const today = new Date();
    return today.toISOString().split('T')[0];
}