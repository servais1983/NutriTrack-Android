// Variables pour les graphiques
let weightChart = null;
let caloriesChart = null;
let proteinChart = null;
let carbsChart = null;
let fatChart = null;
let currentPeriod = 'week';

// Mettre à jour les graphiques de progression
function updateProgressCharts() {
    // Mettre à jour le résumé du poids
    updateWeightSummary();
    
    // Mettre à jour les graphiques
    updateWeightChart();
    updateNutritionCharts();
}

// Mettre à jour le résumé du poids
function updateWeightSummary() {
    // Vérifier que les éléments existent
    const currentWeightElement = document.getElementById('currentWeight');
    const initialWeightElement = document.getElementById('initialWeight');
    const weightChangeElement = document.getElementById('weightChange');
    const bmiValueElement = document.getElementById('bmiValue');
    
    if (!currentWeightElement || !initialWeightElement || !weightChangeElement || !bmiValueElement) return;
    
    // Trier les entrées de poids par date
    const sortedEntries = [...weightEntries].sort((a, b) => new Date(a.date) - new Date(b.date));
    
    // Obtenir le poids initial et actuel
    const initialWeight = sortedEntries.length > 0 ? sortedEntries[0].weight : currentUser.weight;
    const currentWeight = sortedEntries.length > 0 ? sortedEntries[sortedEntries.length - 1].weight : currentUser.weight;
    
    // Calculer la variation de poids
    const weightChange = currentWeight - initialWeight;
    const weightChangeSign = weightChange >= 0 ? '+' : '';
    
    // Calculer l'IMC (poids en kg / (taille en m)²)
    const heightInMeters = currentUser.height / 100;
    const bmi = currentWeight / (heightInMeters * heightInMeters);
    
    // Mettre à jour les éléments
    currentWeightElement.textContent = `${currentWeight.toFixed(1)} kg`;
    initialWeightElement.textContent = `${initialWeight.toFixed(1)} kg`;
    weightChangeElement.textContent = `${weightChangeSign}${weightChange.toFixed(1)} kg`;
    bmiValueElement.textContent = bmi.toFixed(1);
    
    // Appliquer une classe en fonction de la variation de poids
    if (weightChange > 0) {
        weightChangeElement.className = 'positive-change';
    } else if (weightChange < 0) {
        weightChangeElement.className = 'negative-change';
    } else {
        weightChangeElement.className = '';
    }
}

// Mettre à jour le graphique de poids
function updateWeightChart() {
    const ctx = document.getElementById('weightChart');
    if (!ctx) return;
    
    // Trier les entrées de poids par date
    const sortedEntries = [...weightEntries].sort((a, b) => new Date(a.date) - new Date(b.date));
    
    // Préparer les données pour le graphique
    const labels = sortedEntries.map(entry => formatDate(entry.date));
    const data = sortedEntries.map(entry => entry.weight);
    
    // Détruire le graphique existant s'il y en a un
    if (weightChart) {
        weightChart.destroy();
    }
    
    // Créer un nouveau graphique
    weightChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Poids (kg)',
                data: data,
                backgroundColor: 'rgba(76, 175, 80, 0.2)',
                borderColor: 'rgba(76, 175, 80, 1)',
                borderWidth: 2,
                tension: 0.3,
                pointBackgroundColor: 'rgba(76, 175, 80, 1)',
                pointBorderColor: '#fff',
                pointBorderWidth: 2,
                pointRadius: 5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: false,
                    title: {
                        display: true,
                        text: 'Poids (kg)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Date'
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `Poids: ${context.raw} kg`;
                        }
                    }
                }
            }
        }
    });
}

// Mettre à jour les graphiques de nutrition
function updateNutritionCharts() {
    // Obtenir les données nutritionnelles pour la période sélectionnée
    const nutritionData = getNutritionDataForPeriod();
    
    // Mettre à jour chaque graphique
    updateCaloriesChart(nutritionData);
    updateMacroChart('proteinChart', nutritionData.protein, 'Protéines (g)', 'rgba(52, 152, 219, 1)');
    updateMacroChart('carbsChart', nutritionData.carbs, 'Glucides (g)', 'rgba(243, 156, 18, 1)');
    updateMacroChart('fatChart', nutritionData.fat, 'Lipides (g)', 'rgba(231, 76, 60, 1)');
}

// Obtenir les données nutritionnelles pour la période sélectionnée
function getNutritionDataForPeriod() {
    // Déterminer la date de début en fonction de la période
    const today = new Date();
    let startDate;
    
    if (currentPeriod === 'week') {
        // 7 derniers jours
        startDate = new Date(today);
        startDate.setDate(today.getDate() - 6);
    } else if (currentPeriod === 'month') {
        // 30 derniers jours
        startDate = new Date(today);
        startDate.setDate(today.getDate() - 29);
    } else {
        // 12 derniers mois
        startDate = new Date(today);
        startDate.setMonth(today.getMonth() - 11);
    }
    
    // Formater les dates pour la comparaison
    const startDateString = startDate.toISOString().split('T')[0];
    
    // Préparer les structures de données
    const labels = [];
    const calories = [];
    const protein = [];
    const carbs = [];
    const fat = [];
    
    // Générer les labels et initialiser les valeurs
    if (currentPeriod === 'year') {
        // Par mois pour l'année
        for (let i = 0; i < 12; i++) {
            const date = new Date(startDate);
            date.setMonth(startDate.getMonth() + i);
            const monthLabel = date.toLocaleDateString('fr-FR', { month: 'short' });
            labels.push(monthLabel);
            calories.push(0);
            protein.push(0);
            carbs.push(0);
            fat.push(0);
        }
    } else {
        // Par jour pour la semaine ou le mois
        const days = currentPeriod === 'week' ? 7 : 30;
        for (let i = 0; i < days; i++) {
            const date = new Date(startDate);
            date.setDate(startDate.getDate() + i);
            const dayLabel = date.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short' });
            labels.push(dayLabel);
            calories.push(0);
            protein.push(0);
            carbs.push(0);
            fat.push(0);
        }
    }
    
    // Filtrer les entrées de repas pour la période sélectionnée
    const relevantEntries = mealEntries.filter(entry => {
        const entryDate = new Date(entry.date);
        return entryDate >= startDate && entryDate <= today;
    });
    
    // Agréger les données par jour ou par mois
    relevantEntries.forEach(entry => {
        const entryDate = new Date(entry.date);
        let index;
        
        if (currentPeriod === 'year') {
            // Calculer l'index du mois (0-11)
            index = entryDate.getMonth() - startDate.getMonth() + 
                   (entryDate.getFullYear() - startDate.getFullYear()) * 12;
        } else {
            // Calculer l'index du jour (0-6 ou 0-29)
            const diffTime = Math.abs(entryDate - startDate);
            index = Math.floor(diffTime / (1000 * 60 * 60 * 24));
        }
        
        // S'assurer que l'index est valide
        if (index >= 0 && index < labels.length) {
            // Trouver l'aliment correspondant
            const food = foodDatabase.find(food => food.id === entry.foodId);
            if (food) {
                // Calculer la proportion basée sur la quantité
                const proportion = entry.quantity / food.servingSize;
                
                // Ajouter aux totaux
                calories[index] += food.calories * proportion;
                protein[index] += food.protein * proportion;
                carbs[index] += food.carbs * proportion;
                fat[index] += food.fat * proportion;
            }
        }
    });
    
    // Arrondir les valeurs
    for (let i = 0; i < labels.length; i++) {
        calories[i] = Math.round(calories[i]);
        protein[i] = Math.round(protein[i]);
        carbs[i] = Math.round(carbs[i]);
        fat[i] = Math.round(fat[i]);
    }
    
    return { labels, calories, protein, carbs, fat };
}

// Mettre à jour le graphique de calories
function updateCaloriesChart(nutritionData) {
    const ctx = document.getElementById('caloriesChart');
    if (!ctx) return;
    
    // Détruire le graphique existant s'il y en a un
    if (caloriesChart) {
        caloriesChart.destroy();
    }
    
    // Ligne horizontale pour l'objectif de calories
    const targetCalories = currentUser.nutritionGoals.calories;
    
    // Créer un nouveau graphique
    caloriesChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: nutritionData.labels,
            datasets: [{
                label: 'Calories consommées',
                data: nutritionData.calories,
                backgroundColor: 'rgba(76, 175, 80, 0.6)',
                borderColor: 'rgba(76, 175, 80, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Calories (kcal)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Date'
                    }
                }
            },
            plugins: {
                annotation: {
                    annotations: {
                        line1: {
                            type: 'line',
                            yMin: targetCalories,
                            yMax: targetCalories,
                            borderColor: 'rgba(255, 99, 132, 1)',
                            borderWidth: 2,
                            label: {
                                enabled: true,
                                content: 'Objectif',
                                position: 'end'
                            }
                        }
                    }
                }
            }
        }
    });
}

// Mettre à jour un graphique de macronutriment
function updateMacroChart(chartId, data, label, color) {
    const ctx = document.getElementById(chartId);
    if (!ctx) return;
    
    // Détruire le graphique existant s'il y en a un
    let chart;
    if (chartId === 'proteinChart') {
        if (proteinChart) proteinChart.destroy();
        chart = proteinChart;
    } else if (chartId === 'carbsChart') {
        if (carbsChart) carbsChart.destroy();
        chart = carbsChart;
    } else if (chartId === 'fatChart') {
        if (fatChart) fatChart.destroy();
        chart = fatChart;
    }
    
    // Créer un nouveau graphique
    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: nutritionData.labels,
            datasets: [{
                label: label,
                data: data,
                backgroundColor: color.replace('1)', '0.2)'),
                borderColor: color,
                borderWidth: 2,
                tension: 0.1,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
    
    // Sauvegarder la référence du graphique
    if (chartId === 'proteinChart') {
        proteinChart = chart;
    } else if (chartId === 'carbsChart') {
        carbsChart = chart;
    } else if (chartId === 'fatChart') {
        fatChart = chart;
    }
}

// Gérer la sélection d'un onglet de progression
function handleProgressTab(event) {
    // Retirer la classe active de tous les boutons et contenus
    document.querySelectorAll('.progress-tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    document.querySelectorAll('.progress-content').forEach(content => {
        content.classList.remove('active');
    });
    
    // Ajouter la classe active au bouton cliqué
    event.target.classList.add('active');
    
    // Afficher le contenu correspondant
    const tabId = event.target.dataset.tab;
    const content = document.getElementById(tabId + 'Progress');
    if (content) {
        content.classList.add('active');
    }
    
    // Mettre à jour les graphiques si nécessaire
    if (tabId === 'weight') {
        updateWeightChart();
    } else if (tabId === 'nutrition') {
        updateNutritionCharts();
    }
}

// Gérer la sélection de période
function handlePeriodSelect(event) {
    // Retirer la classe active de tous les boutons
    document.querySelectorAll('.period-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Ajouter la classe active au bouton cliqué
    event.target.classList.add('active');
    
    // Mettre à jour la période sélectionnée
    currentPeriod = event.target.dataset.period;
    
    // Mettre à jour les graphiques
    updateNutritionCharts();
}

// Gérer la soumission du formulaire d'ajout de poids
function handleWeightSubmit(event) {
    event.preventDefault();
    
    // Récupérer les valeurs du formulaire
    const date = document.getElementById('weightDate').value;
    const weight = parseFloat(document.getElementById('weightValue').value);
    const notes = document.getElementById('weightNotes').value.trim();
    
    // Valider les données
    if (!date || !weight) {
        showNotification('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    // Vérifier si une entrée existe déjà pour cette date
    const existingEntryIndex = weightEntries.findIndex(entry => entry.date === date);
    
    if (existingEntryIndex !== -1) {
        // Mettre à jour l'entrée existante
        weightEntries[existingEntryIndex].weight = weight;
        weightEntries[existingEntryIndex].notes = notes;
    } else {
        // Ajouter une nouvelle entrée
        weightEntries.push({
            id: generateId(),
            date: date,
            weight: weight,
            notes: notes
        });
    }
    
    // Mettre à jour le poids actuel de l'utilisateur
    const today = new Date().toISOString().split('T')[0];
    if (date === today) {
        currentUser.weight = weight;
    }
    
    // Sauvegarder les données
    saveData();
    
    // Mettre à jour l'interface
    updateProgressCharts();
    updateProfileUI();
    
    // Fermer la modale
    closeModal('addWeightModal');
    
    // Afficher une notification
    showNotification('Poids enregistré avec succès');
}