// Mettre à jour la liste des aliments
function updateFoodListUI() {
    const foodList = document.getElementById('foodList');
    if (!foodList) return;
    
    // Vider la liste actuelle
    foodList.innerHTML = '';
    
    // Obtenir le filtre actif
    const activeFilterBtn = document.querySelector('.filter-btn.active');
    const filterType = activeFilterBtn ? activeFilterBtn.dataset.filter : 'all';
    
    // Filtrer les aliments selon le type de filtre
    let filteredFoods = [...foodDatabase];
    
    if (filterType === 'favorites') {
        filteredFoods = filteredFoods.filter(food => food.favorite);
    } else if (filterType === 'recent') {
        // Pour simplifier, on affiche les 5 premiers aliments comme "récents"
        filteredFoods = filteredFoods.slice(0, 5);
    } else if (filterType === 'custom') {
        filteredFoods = filteredFoods.filter(food => food.custom);
    }
    
    // Recherche par texte si disponible
    const searchInput = document.getElementById('foodSearch');
    if (searchInput && searchInput.value.trim() !== '') {
        const searchTerm = searchInput.value.trim().toLowerCase();
        filteredFoods = filteredFoods.filter(food => 
            food.name.toLowerCase().includes(searchTerm)
        );
    }
    
    // Afficher un message si aucun aliment n'est trouvé
    if (filteredFoods.length === 0) {
        foodList.innerHTML = `<p class="empty-meal">Aucun aliment trouvé</p>`;
        return;
    }
    
    // Ajouter chaque aliment à la liste
    filteredFoods.forEach(food => {
        const foodCard = document.createElement('div');
        foodCard.className = 'food-card';
        foodCard.innerHTML = `
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
                        <i class="fas fa-plus"></i> Ajouter au journal
                    </button>
                    ${food.custom ? `
                    <button class="food-action-btn" onclick="editCustomFood('${food.id}')">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    ` : ''}
                </div>
            </div>
        `;
        
        foodList.appendChild(foodCard);
    });
    
    // Configurer la recherche en temps réel
    if (searchInput && !searchInput.hasAttribute('data-event-attached')) {
        searchInput.addEventListener('input', updateFoodListUI);
        searchInput.setAttribute('data-event-attached', 'true');
    }
}

// Gérer le filtre sur les aliments
function handleFoodFilter(event) {
    // Retirer la classe active de tous les boutons
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Ajouter la classe active au bouton cliqué
    event.target.classList.add('active');
    
    // Mettre à jour la liste d'aliments
    updateFoodListUI();
}

// Basculer le statut favori d'un aliment
function toggleFavorite(foodId) {
    // Trouver l'aliment dans la base de données
    const foodIndex = foodDatabase.findIndex(food => food.id === foodId);
    
    if (foodIndex !== -1) {
        // Inverser le statut favori
        foodDatabase[foodIndex].favorite = !foodDatabase[foodIndex].favorite;
        
        // Sauvegarder les données
        saveData();
        
        // Mettre à jour l'interface
        updateFoodListUI();
        
        // Mettre à jour également la modale si elle est ouverte
        updateModalFoodList();
    }
}

// Modifier un aliment personnalisé
function editCustomFood(foodId) {
    // Trouver l'aliment dans la base de données
    const food = foodDatabase.find(food => food.id === foodId);
    
    if (food && food.custom) {
        // Stocker l'ID pour la mise à jour
        sessionStorage.setItem('editFoodId', foodId);
        
        // Pré-remplir le formulaire
        document.getElementById('customFoodName').value = food.name;
        document.getElementById('customFoodServing').value = food.servingSize;
        document.getElementById('customFoodCalories').value = food.calories;
        document.getElementById('customFoodProtein').value = food.protein;
        document.getElementById('customFoodCarbs').value = food.carbs;
        document.getElementById('customFoodFat').value = food.fat;
        
        // Ouvrir la modale
        openModal('addCustomFoodModal');
    }
}

// Gérer la soumission du formulaire d'aliment personnalisé
function handleCustomFoodSubmit(event) {
    event.preventDefault();
    
    // Récupérer les valeurs du formulaire
    const name = document.getElementById('customFoodName').value.trim();
    const servingSize = parseFloat(document.getElementById('customFoodServing').value);
    const calories = parseFloat(document.getElementById('customFoodCalories').value);
    const protein = parseFloat(document.getElementById('customFoodProtein').value);
    const carbs = parseFloat(document.getElementById('customFoodCarbs').value);
    const fat = parseFloat(document.getElementById('customFoodFat').value);
    
    // Valider les données
    if (!name || !servingSize || !calories || !protein || !carbs || !fat) {
        showNotification('Veuillez remplir tous les champs', 'error');
        return;
    }
    
    // Vérifier si nous éditons un aliment existant
    const editFoodId = sessionStorage.getItem('editFoodId');
    
    if (editFoodId) {
        // Trouver l'index de l'aliment dans le tableau
        const foodIndex = foodDatabase.findIndex(food => food.id === editFoodId);
        
        if (foodIndex !== -1) {
            // Mettre à jour l'aliment existant
            foodDatabase[foodIndex].name = name;
            foodDatabase[foodIndex].servingSize = servingSize;
            foodDatabase[foodIndex].calories = calories;
            foodDatabase[foodIndex].protein = protein;
            foodDatabase[foodIndex].carbs = carbs;
            foodDatabase[foodIndex].fat = fat;
            
            // Nettoyer la session storage
            sessionStorage.removeItem('editFoodId');
            
            // Sauvegarder les données
            saveData();
            
            // Mettre à jour l'interface
            updateFoodListUI();
            
            // Fermer la modale
            closeModal('addCustomFoodModal');
            
            // Afficher une notification
            showNotification('Aliment mis à jour avec succès');
            
            return;
        }
    }
    
    // Ajouter un nouvel aliment
    const newFood = {
        id: generateId(),
        name: name,
        servingSize: servingSize,
        calories: calories,
        protein: protein,
        carbs: carbs,
        fat: fat,
        favorite: false,
        custom: true
    };
    
    // Ajouter à la base de données
    foodDatabase.push(newFood);
    
    // Sauvegarder les données
    saveData();
    
    // Mettre à jour l'interface
    updateFoodListUI();
    
    // Fermer la modale
    closeModal('addCustomFoodModal');
    
    // Afficher une notification
    showNotification('Aliment ajouté avec succès');
}

// Démarrer le scanner de code-barres
function startScanner() {
    const scanner = document.getElementById('scanner');
    const scannerPlaceholder = document.querySelector('.scanner-placeholder');
    const scanOverlay = document.querySelector('.scan-overlay');
    
    if (scanner) {
        // Afficher le scanner et masquer le placeholder
        scanner.style.display = 'block';
        if (scannerPlaceholder) scannerPlaceholder.style.display = 'none';
        if (scanOverlay) scanOverlay.style.display = 'block';
        
        // Accéder à la caméra
        navigator.mediaDevices.getUserMedia({ video: { facingMode: 'environment' } })
            .then(function(stream) {
                scanner.srcObject = stream;
                scanner.play();
                
                // Initialiser Quagga pour la détection de code-barres
                initQuagga(scanner);
            })
            .catch(function(error) {
                console.error('Erreur d\'accès à la caméra:', error);
                showNotification('Impossible d\'accéder à la caméra', 'error');
            });
    }
}

// Arrêter le scanner
function stopScanner() {
    const scanner = document.getElementById('scanner');
    const scannerPlaceholder = document.querySelector('.scanner-placeholder');
    const scanOverlay = document.querySelector('.scan-overlay');
    
    // Arrêter Quagga
    if (typeof Quagga !== 'undefined') {
        Quagga.stop();
    }
    
    if (scanner && scanner.srcObject) {
        // Arrêter tous les tracks de la caméra
        scanner.srcObject.getTracks().forEach(track => track.stop());
        scanner.srcObject = null;
        
        // Masquer le scanner et afficher le placeholder
        scanner.style.display = 'none';
        if (scannerPlaceholder) scannerPlaceholder.style.display = 'flex';
        if (scanOverlay) scanOverlay.style.display = 'none';
    }
}

// Initialiser Quagga pour la détection de code-barres
function initQuagga(videoElement) {
    if (typeof Quagga === 'undefined') {
        console.error('Quagga n\'est pas chargé');
        return;
    }
    
    Quagga.init({
        inputStream: {
            name: "Live",
            type: "LiveStream",
            target: videoElement,
            constraints: {
                width: { min: 640 },
                height: { min: 480 },
                facingMode: "environment"
            }
        },
        locator: {
            patchSize: "medium",
            halfSample: true
        },
        numOfWorkers: 2,
        decoder: {
            readers: [
                "ean_reader",
                "ean_8_reader",
                "code_39_reader",
                "code_128_reader",
                "upc_reader",
                "upc_e_reader"
            ]
        },
        locate: true
    }, function(err) {
        if (err) {
            console.error('Erreur d\'initialisation de Quagga:', err);
            return;
        }
        
        console.log('Quagga initialisé');
        Quagga.start();
        
        // Ajouter un écouteur pour les codes-barres détectés
        Quagga.onDetected(handleBarcodeScan);
    });
}

// Gérer un code-barres détecté
function handleBarcodeScan(result) {
    // Arrêter le scanner
    stopScanner();
    
    // Afficher le résultat
    const barcode = result.codeResult.code;
    console.log('Code-barres détecté:', barcode);
    
    // Dans une vraie application, on ferait une requête à une API d'aliments
    // Pour cette démo, on crée un aliment fictif basé sur le code-barres
    const scannedFood = {
        id: generateId(),
        name: `Aliment scanné (${barcode})`,
        calories: Math.floor(Math.random() * 300) + 100,
        protein: Math.floor(Math.random() * 20) + 5,
        carbs: Math.floor(Math.random() * 30) + 10,
        fat: Math.floor(Math.random() * 15) + 2,
        servingSize: 100,
        favorite: false,
        custom: true
    };
    
    // Ajouter l'aliment à la base de données
    foodDatabase.push(scannedFood);
    
    // Sauvegarder les données
    saveData();
    
    // Afficher les informations de l'aliment scanné
    const scannedFoodInfo = document.getElementById('scannedFoodInfo');
    const scanResult = document.getElementById('scanResult');
    
    if (scannedFoodInfo && scanResult) {
        scannedFoodInfo.innerHTML = `
            <h4>${scannedFood.name}</h4>
            <div class="nutrients-grid">
                <div class="nutrient-item">
                    <span class="nutrient-label">Calories:</span>
                    <span>${scannedFood.calories} kcal</span>
                </div>
                <div class="nutrient-item">
                    <span class="nutrient-label">Protéines:</span>
                    <span>${scannedFood.protein}g</span>
                </div>
                <div class="nutrient-item">
                    <span class="nutrient-label">Glucides:</span>
                    <span>${scannedFood.carbs}g</span>
                </div>
                <div class="nutrient-item">
                    <span class="nutrient-label">Lipides:</span>
                    <span>${scannedFood.fat}g</span>
                </div>
            </div>
            <div class="food-card-actions" style="margin-top: 15px;">
                <button class="food-action-btn" onclick="selectFoodToAdd('${scannedFood.id}')">
                    <i class="fas fa-plus"></i> Ajouter au journal
                </button>
            </div>
        `;
        
        scanResult.style.display = 'block';
    }
    
    // Notification
    showNotification('Code-barres détecté: ' + barcode);
}