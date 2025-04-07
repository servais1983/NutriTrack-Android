// Gérer la navigation entre les onglets
function showTab(tabId) {
    // Masquer tous les onglets
    const tabContents = document.querySelectorAll('.tab-content');
    tabContents.forEach(tab => {
        tab.classList.remove('active');
    });
    
    // Afficher l'onglet demandé
    const selectedTab = document.getElementById(tabId);
    if (selectedTab) {
        selectedTab.classList.add('active');
    }
    
    // Mettre à jour la classe active sur les liens de navigation
    const navLinks = document.querySelectorAll('nav ul li a');
    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('onclick').includes(`showTab('${tabId}')`)) {
            link.classList.add('active');
        }
    });
    
    // Actions spécifiques selon l'onglet
    if (tabId === 'dashboard') {
        updateDashboardUI();
    } else if (tabId === 'food') {
        updateFoodListUI();
    } else if (tabId === 'progress') {
        updateProgressCharts();
    } else if (tabId === 'profile') {
        updateProfileUI();
    }
}

// Gérer l'affichage des modales
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
        // Empêcher le défilement sur le body
        document.body.style.overflow = 'hidden';
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
        // Rétablir le défilement sur le body
        document.body.style.overflow = '';
    }
}

// Ouvrir la modale d'ajout d'aliment avec le type de repas sélectionné
function openAddFoodModal(mealType) {
    // Stocker le type de repas sélectionné pour utilisation ultérieure
    sessionStorage.setItem('selectedMealType', mealType);
    
    // Mettre à jour la liste d'aliments dans la modale
    updateModalFoodList();
    
    // Ouvrir la modale
    openModal('addFoodModal');
}

// Mettre à jour la liste d'aliments dans la modale
function updateModalFoodList() {
    const modalFoodList = document.getElementById('modalFoodList');
    if (!modalFoodList) return;
    
    // Vider la liste actuelle
    modalFoodList.innerHTML = '';
    
    // Ajouter chaque aliment à la liste
    foodDatabase.forEach(food => {
        const foodItem = document.createElement('div');
        foodItem.className = 'food-card';
        foodItem.innerHTML = `
            <div class="food-card-header">
                <h3>${food.name}</h3>
                <button class="favorite-btn ${food.favorite ? 'active' : ''}" onclick="toggleFavorite('${food.id}')">
                    <i class="fas fa-star"></i>
                </button>
            </div>
            <div class="food-card-body">
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
                <div class="food-card-actions">
                    <button class="food-action-btn" onclick="selectFoodToAdd('${food.id}')">
                        <i class="fas fa-plus"></i> Ajouter
                    </button>
                </div>
            </div>
        `;
        
        modalFoodList.appendChild(foodItem);
    });
}

// Fonction pour sélectionner un aliment à ajouter au journal
function selectFoodToAdd(foodId) {
    // Trouver l'aliment dans la base de données
    const selectedFood = foodDatabase.find(food => food.id === foodId);
    
    if (selectedFood) {
        // Stocker l'ID de l'aliment sélectionné
        sessionStorage.setItem('selectedFoodId', foodId);
        
        // Afficher les informations de l'aliment dans la modale d'ajout
        const selectedFoodInfo = document.getElementById('selectedFoodInfo');
        if (selectedFoodInfo) {
            selectedFoodInfo.innerHTML = `
                <h4>${selectedFood.name}</h4>
                <div class="nutrients-grid">
                    <div class="nutrient-item">
                        <span class="nutrient-label">Calories:</span>
                        <span>${selectedFood.calories} kcal</span>
                    </div>
                    <div class="nutrient-item">
                        <span class="nutrient-label">Protéines:</span>
                        <span>${selectedFood.protein}g</span>
                    </div>
                    <div class="nutrient-item">
                        <span class="nutrient-label">Glucides:</span>
                        <span>${selectedFood.carbs}g</span>
                    </div>
                    <div class="nutrient-item">
                        <span class="nutrient-label">Lipides:</span>
                        <span>${selectedFood.fat}g</span>
                    </div>
                </div>
                <p>Quantité de référence: ${selectedFood.servingSize}g</p>
            `;
        }
        
        // Pré-sélectionner le type de repas si disponible
        const mealType = sessionStorage.getItem('selectedMealType');
        if (mealType && document.getElementById('foodMealType')) {
            document.getElementById('foodMealType').value = mealType;
        }
        
        // Fermer la modale de sélection d'aliment
        closeModal('addFoodModal');
        
        // Ouvrir la modale d'ajout au journal
        openModal('addFoodToLogModal');
    }
}

// Ouvrir la modale pour ajouter un aliment personnalisé
function openAddCustomFoodModal() {
    // Réinitialiser le formulaire
    const form = document.getElementById('customFoodForm');
    if (form) {
        form.reset();
    }
    
    // Ouvrir la modale
    openModal('addCustomFoodModal');
}

// Ouvrir la modale de scan
function openScanModal() {
    // Réinitialiser l'état du scan
    const scanResult = document.getElementById('scanResult');
    if (scanResult) {
        scanResult.style.display = 'none';
    }
    
    const scannedFoodInfo = document.getElementById('scannedFoodInfo');
    if (scannedFoodInfo) {
        scannedFoodInfo.innerHTML = '';
    }
    
    // Ouvrir la modale
    openModal('scanModal');
}

// Ouvrir la modale pour ajouter une entrée de poids
function openAddWeightModal() {
    // Réinitialiser le formulaire
    const form = document.getElementById('weightForm');
    if (form) {
        form.reset();
        // Pré-remplir avec la date du jour
        const weightDateInput = document.getElementById('weightDate');
        if (weightDateInput) {
            weightDateInput.value = getCurrentDate();
        }
    }
    
    // Ouvrir la modale
    openModal('addWeightModal');
}