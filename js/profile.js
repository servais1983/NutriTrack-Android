// Mettre à jour l'interface du profil
function updateProfileUI() {
    // Mettre à jour le nom affiché dans l'en-tête du profil
    const profileName = document.getElementById('profileName');
    if (profileName) {
        profileName.textContent = currentUser.name || 'Utilisateur';
    }
    
    // Mettre à jour les valeurs du formulaire
    const profileForm = document.getElementById('profileForm');
    if (!profileForm) return;
    
    // Informations personnelles
    document.getElementById('profileNameInput').value = currentUser.name || '';
    document.getElementById('profileEmail').value = currentUser.email || '';
    document.getElementById('profileBirthdate').value = currentUser.birthdate || '';
    document.getElementById('profileGender').value = currentUser.gender || '';
    
    // Mesures corporelles
    document.getElementById('profileHeight').value = currentUser.height || '';
    document.getElementById('profileWeight').value = currentUser.weight || '';
    document.getElementById('profileActivity').value = currentUser.activityLevel || '1.375';
    document.getElementById('profileGoal').value = currentUser.goal || 'maintain';
    
    // Objectifs nutritionnels
    const goals = currentUser.nutritionGoals || { calories: 2000, protein: 150, carbs: 250, fat: 65 };
    document.getElementById('profileCalories').value = goals.calories || '';
    document.getElementById('profileProtein').value = goals.protein || '';
    document.getElementById('profileCarbs').value = goals.carbs || '';
    document.getElementById('profileFat').value = goals.fat || '';
    
    // Calculer et afficher les besoins caloriques estimés
    calculateAndDisplayCalorieNeeds();
}

// Calculer et afficher les besoins caloriques estimés
function calculateAndDisplayCalorieNeeds() {
    const calorieNeeds = document.getElementById('calorieNeeds');
    if (!calorieNeeds) return;
    
    // Vérifier que les données nécessaires sont disponibles
    if (!currentUser.height || !currentUser.weight || !currentUser.gender || !currentUser.birthdate) {
        calorieNeeds.textContent = 'Données insuffisantes';
        return;
    }
    
    // Calculer l'âge à partir de la date de naissance
    const birthDate = new Date(currentUser.birthdate);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    
    // Formule de Mifflin-St Jeor pour calculer le métabolisme de base (BMR)
    let bmr;
    if (currentUser.gender === 'homme') {
        bmr = 10 * currentUser.weight + 6.25 * currentUser.height - 5 * age + 5;
    } else {
        bmr = 10 * currentUser.weight + 6.25 * currentUser.height - 5 * age - 161;
    }
    
    // Multiplier par le niveau d'activité
    const tdee = bmr * currentUser.activityLevel;
    
    // Ajuster en fonction de l'objectif
    let calories;
    if (currentUser.goal === 'loss') {
        calories = tdee - 500; // Déficit de 500 kcal pour la perte de poids
    } else if (currentUser.goal === 'gain') {
        calories = tdee + 500; // Surplus de 500 kcal pour la prise de poids
    } else {
        calories = tdee; // Maintien du poids
    }
    
    // Arrondir et afficher
    calorieNeeds.textContent = Math.round(calories);
    
    // Mettre à jour les objectifs nutritionnels suggérés
    document.getElementById('profileCalories').value = Math.round(calories);
    
    // Calculer les macronutriments suggérés en fonction de l'objectif
    let proteinPerKg, carbsPercent, fatPercent;
    
    if (currentUser.goal === 'loss') {
        proteinPerKg = 2.2; // 2.2g par kg pour préserver la masse musculaire
        carbsPercent = 0.40; // 40% des calories
        fatPercent = 0.25; // 25% des calories
    } else if (currentUser.goal === 'gain') {
        proteinPerKg = 1.8; // 1.8g par kg pour la prise de muscle
        carbsPercent = 0.45; // 45% des calories
        fatPercent = 0.25; // 25% des calories
    } else {
        proteinPerKg = 1.6; // 1.6g par kg pour le maintien
        carbsPercent = 0.45; // 45% des calories
        fatPercent = 0.30; // 30% des calories
    }
    
    // Calculer les grammes de chaque macronutriment
    const protein = Math.round(currentUser.weight * proteinPerKg);
    const proteinCalories = protein * 4; // 4 kcal par gramme de protéine
    
    const carbsCalories = calories * carbsPercent;
    const carbs = Math.round(carbsCalories / 4); // 4 kcal par gramme de glucides
    
    const fatCalories = calories * fatPercent;
    const fat = Math.round(fatCalories / 9); // 9 kcal par gramme de lipides
    
    // Mettre à jour les champs
    document.getElementById('profileProtein').value = protein;
    document.getElementById('profileCarbs').value = carbs;
    document.getElementById('profileFat').value = fat;
}

// Gérer la soumission du formulaire de profil
function handleProfileSubmit(event) {
    event.preventDefault();
    
    // Récupérer les valeurs du formulaire
    const name = document.getElementById('profileNameInput').value.trim();
    const email = document.getElementById('profileEmail').value.trim();
    const birthdate = document.getElementById('profileBirthdate').value;
    const gender = document.getElementById('profileGender').value;
    const height = parseFloat(document.getElementById('profileHeight').value);
    const weight = parseFloat(document.getElementById('profileWeight').value);
    const activityLevel = parseFloat(document.getElementById('profileActivity').value);
    const goal = document.getElementById('profileGoal').value;
    const calories = parseInt(document.getElementById('profileCalories').value);
    const protein = parseInt(document.getElementById('profileProtein').value);
    const carbs = parseInt(document.getElementById('profileCarbs').value);
    const fat = parseInt(document.getElementById('profileFat').value);
    
    // Valider les données essentielles
    if (!height || !weight || !activityLevel || !goal) {
        showNotification('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    // Mettre à jour les données de l'utilisateur
    currentUser.name = name;
    currentUser.email = email;
    currentUser.birthdate = birthdate;
    currentUser.gender = gender;
    currentUser.height = height;
    currentUser.weight = weight;
    currentUser.activityLevel = activityLevel;
    currentUser.goal = goal;
    
    // Mettre à jour les objectifs nutritionnels
    currentUser.nutritionGoals = {
        calories: calories || 2000,
        protein: protein || 150,
        carbs: carbs || 250,
        fat: fat || 65
    };
    
    // Mettre à jour l'entrée de poids si nécessaire
    const today = new Date().toISOString().split('T')[0];
    const existingWeightEntry = weightEntries.find(entry => entry.date === today);
    
    if (existingWeightEntry) {
        existingWeightEntry.weight = weight;
    } else {
        // Ajouter une nouvelle entrée de poids
        weightEntries.push({
            id: generateId(),
            date: today,
            weight: weight,
            notes: 'Mise à jour depuis le profil'
        });
    }
    
    // Sauvegarder les données
    saveData();
    
    // Mettre à jour l'interface
    updateProfileUI();
    
    // Mettre à jour aussi le tableau de bord et les graphiques
    updateDashboardUI();
    updateProgressCharts();
    
    // Afficher une notification
    showNotification('Profil mis à jour avec succès');
}