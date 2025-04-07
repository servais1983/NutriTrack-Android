// Variables pour le tableau de bord
let currentDashboardDate = new Date();

// Mettre à jour l'interface du tableau de bord
function updateDashboardUI() {
    // Mettre à jour la date affichée
    updateDisplayedDate();
    
    // Mettre à jour les anneaux de progression
    updateNutritionRings();
    
    // Mettre à jour les repas affichés
    updateMeals();
}

// Mettre à jour la date affichée
function updateDisplayedDate() {
    const dateElement = document.getElementById('currentDate');
    if (!dateElement) return;
    
    const today = new Date();
    const isToday = currentDashboardDate.toDateString() === today.toDateString();
    const isYesterday = new Date(today.setDate(today.getDate() - 1)).toDateString() === currentDashboardDate.toDateString();
    
    let displayText;
    if (isToday) {
        displayText = "Aujourd'hui";
    } else if (isYesterday) {
        displayText = "Hier";
    } else {
        displayText = currentDashboardDate.toLocaleDateString('fr-FR', {
            weekday: 'long',
            day: 'numeric',
            month: 'long'
        });
    }
    
    dateElement.textContent = displayText;
}

// Passer au jour précédent
function previousDay() {
    currentDashboardDate.setDate(currentDashboardDate.getDate() - 1);
    updateDashboardUI();
}

// Passer au jour suivant
function nextDay() {
    const today = new Date();
    // Ne pas permettre de dépasser aujourd'hui
    if (currentDashboardDate.toDateString() !== today.toDateString()) {
        currentDashboardDate.setDate(currentDashboardDate.getDate() + 1);
        updateDashboardUI();
    }
}

// Mettre à jour les anneaux de progression
function updateNutritionRings() {
    // Récupérer les entrées de repas pour la date sélectionnée
    const formattedDate = currentDashboardDate.toISOString().split('T')[0];
    const entries = mealEntries.filter(entry => entry.date === formattedDate);
    
    // Calculer les totaux nutritionnels pour la journée
    const totals = calculateDailyTotals(entries);
    
    // Obtenir les objectifs de l'utilisateur
    const goals = currentUser.nutritionGoals;
    
    // Mettre à jour les anneaux et les valeurs
    updateProgressRing('calories', totals.calories, goals.calories);
    updateProgressRing('protein', totals.protein, goals.protein);
    updateProgressRing('carbs', totals.carbs, goals.carbs);
    updateProgressRing('fat', totals.fat, goals.fat);
}

// Mettre à jour un anneau de progression spécifique
function updateProgressRing(nutrient, value, goal) {
    // Mettre à jour les textes
    const consumedElement = document.getElementById(`${nutrient}Consumed`);
    const goalElement = document.getElementById(`${nutrient}Goal`);
    
    if (consumedElement && goalElement) {
        consumedElement.textContent = Math.round(value);
        goalElement.textContent = goal;
    }
    
    // Mettre à jour l'anneau de progression
    const ring = document.getElementById(`${nutrient}Ring`);
    if (ring) {
        // Calculer le pourcentage (max 100%)
        const percent = Math.min(value / goal * 100, 100);
        
        // Calculer la valeur de stroke-dashoffset
        // Circonsférence du cercle = 2 * PI * rayon = 2 * PI * 45 = 283
        const circumference = 283;
        const offset = circumference - (percent / 100 * circumference);
        
        ring.style.strokeDashoffset = offset;
    }
}

// Calculer les totaux nutritionnels pour une journée
function calculateDailyTotals(entries) {
    return entries.reduce((totals, entry) => {
        // Trouver l'aliment correspondant
        const food = foodDatabase.find(food => food.id === entry.foodId);
        if (!food) return totals;
        
        // Calculer la proportion basée sur la quantité
        const proportion = entry.quantity / food.servingSize;
        
        // Ajouter aux totaux
        totals.calories += food.calories * proportion;
        totals.protein += food.protein * proportion;
        totals.carbs += food.carbs * proportion;
        totals.fat += food.fat * proportion;
        
        return totals;
    }, { calories: 0, protein: 0, carbs: 0, fat: 0 });
}

// Mettre à jour les repas affichés
function updateMeals() {
    // Récupérer les entrées de repas pour la date sélectionnée
    const formattedDate = currentDashboardDate.toISOString().split('T')[0];
    const entries = mealEntries.filter(entry => entry.date === formattedDate);
    
    // Grouper les entrées par type de repas
    const mealTypes = ['breakfast', 'lunch', 'dinner', 'snacks'];
    
    // Mettre à jour chaque section de repas
    mealTypes.forEach(mealType => {
        const mealItems = document.getElementById(`${mealType}Items`);
        if (!mealItems) return;
        
        // Filtrer les entrées pour ce type de repas
        const mealEntries = entries.filter(entry => entry.mealType === mealType);
        
        if (mealEntries.length === 0) {
            // Afficher un message si aucun aliment n'est ajouté
            mealItems.innerHTML = `<p class="empty-meal">Aucun aliment ajouté</p>`;
        } else {
            // Vider le contenu actuel
            mealItems.innerHTML = '';
            
            // Ajouter chaque entrée de repas
            mealEntries.forEach(entry => {
                // Trouver l'aliment correspondant
                const food = foodDatabase.find(food => food.id === entry.foodId);
                if (!food) return;
                
                // Calculer les valeurs nutritionnelles en fonction de la quantité
                const proportion = entry.quantity / food.servingSize;
                const calories = Math.round(food.calories * proportion);
                const protein = (food.protein * proportion).toFixed(1);
                const carbs = (food.carbs * proportion).toFixed(1);
                const fat = (food.fat * proportion).toFixed(1);
                
                // Créer l'élément HTML pour l'entrée
                const foodItem = document.createElement('div');
                foodItem.className = 'food-item';
                foodItem.innerHTML = `
                    <div class="food-item-info">
                        <div class="food-item-name">${food.name}</div>
                        <div class="food-item-details">${entry.quantity}g - ${calories} kcal (P: ${protein}g, G: ${carbs}g, L: ${fat}g)</div>
                    </div>
                    <div class="food-item-actions">
                        <button class="food-item-action edit-btn" onclick="editMealEntry('${entry.id}')"><i class="fas fa-edit"></i></button>
                        <button class="food-item-action delete-btn" onclick="deleteMealEntry('${entry.id}')"><i class="fas fa-trash"></i></button>
                    </div>
                `;
                
                mealItems.appendChild(foodItem);
            });
        }
    });
}

// Modifier une entrée de repas
function editMealEntry(entryId) {
    // Trouver l'entrée dans le tableau
    const entry = mealEntries.find(entry => entry.id === entryId);
    if (!entry) return;
    
    // Trouver l'aliment correspondant
    const food = foodDatabase.find(food => food.id === entry.foodId);
    if (!food) return;
    
    // Stocker l'ID de l'entrée pour la mise à jour
    sessionStorage.setItem('editEntryId', entryId);
    
    // Pré-remplir le formulaire d'ajout d'aliment
    const selectedFoodInfo = document.getElementById('selectedFoodInfo');
    if (selectedFoodInfo) {
        selectedFoodInfo.innerHTML = `
            <h4>${food.name}</h4>
            <div class="nutrients-grid">
                <div class="nutrient-item">
                    <span class="nutrient-label">Calories:</span>
                    <span>${food.calories} kcal</span>
                </div>
                <div class="nutrient-item">
                    <span class="nutrient-label">Protéines:</span>
                    <span>${food.protein}g</span>
                </div>
                <div class="nutrient-item">
                    <span class="nutrient-label">Glucides:</span>
                    <span>${food.carbs}g</span>
                </div>
                <div class="nutrient-item">
                    <span class="nutrient-label">Lipides:</span>
                    <span>${food.fat}g</span>
                </div>
            </div>
            <p>Quantité de référence: ${food.servingSize}g</p>
        `;
    }
    
    // Pré-remplir la quantité et le type de repas
    const quantityInput = document.getElementById('foodQuantity');
    const mealTypeSelect = document.getElementById('foodMealType');
    
    if (quantityInput) quantityInput.value = entry.quantity;
    if (mealTypeSelect) mealTypeSelect.value = entry.mealType;
    
    // Stocker l'ID de l'aliment sélectionné
    sessionStorage.setItem('selectedFoodId', entry.foodId);
    
    // Ouvrir la modale d'ajout au journal
    openModal('addFoodToLogModal');
}

// Supprimer une entrée de repas
function deleteMealEntry(entryId) {
    // Demander confirmation
    if (confirm('Êtes-vous sûr de vouloir supprimer cet aliment ?')) {
        // Trouver l'index de l'entrée dans le tableau
        const entryIndex = mealEntries.findIndex(entry => entry.id === entryId);
        
        if (entryIndex !== -1) {
            // Supprimer l'entrée
            mealEntries.splice(entryIndex, 1);
            
            // Sauvegarder les données
            saveData();
            
            // Mettre à jour l'interface
            updateDashboardUI();
            
            // Afficher une notification
            showNotification('Aliment supprimé avec succès');
        }
    }
}

// Gérer la soumission du formulaire d'ajout d'aliment au journal
function handleAddFoodToLog(event) {
    event.preventDefault();
    
    // Récupérer les valeurs du formulaire
    const quantity = parseFloat(document.getElementById('foodQuantity').value);
    const mealType = document.getElementById('foodMealType').value;
    const foodId = sessionStorage.getItem('selectedFoodId');
    
    if (!quantity || !mealType || !foodId) {
        showNotification('Veuillez remplir tous les champs', 'error');
        return;
    }
    
    // Vérifier si nous éditons une entrée existante
    const editEntryId = sessionStorage.getItem('editEntryId');
    
    if (editEntryId) {
        // Trouver l'index de l'entrée dans le tableau
        const entryIndex = mealEntries.findIndex(entry => entry.id === editEntryId);
        
        if (entryIndex !== -1) {
            // Mettre à jour l'entrée existante
            mealEntries[entryIndex].quantity = quantity;
            mealEntries[entryIndex].mealType = mealType;
            
            // Nettoyer la session storage
            sessionStorage.removeItem('editEntryId');
            
            // Sauvegarder les données
            saveData();
            
            // Mettre à jour l'interface
            updateDashboardUI();
            
            // Fermer la modale
            closeModal('addFoodToLogModal');
            
            // Afficher une notification
            showNotification('Aliment mis à jour avec succès');
            
            return;
        }
    }
    
    // Ajouter une nouvelle entrée
    const newEntry = {
        id: generateId(),
        date: currentDashboardDate.toISOString().split('T')[0],
        foodId: foodId,
        quantity: quantity,
        mealType: mealType
    };
    
    // Ajouter au tableau des entrées
    mealEntries.push(newEntry);
    
    // Sauvegarder les données
    saveData();
    
    // Mettre à jour l'interface
    updateDashboardUI();
    
    // Fermer la modale
    closeModal('addFoodToLogModal');
    
    // Afficher une notification
    showNotification('Aliment ajouté avec succès');
}