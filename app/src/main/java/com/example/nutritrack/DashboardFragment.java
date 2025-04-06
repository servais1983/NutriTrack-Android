package com.example.nutritrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritrack.adapter.FoodEntryAdapter;
import com.example.nutritrack.database.entity.FoodEntryEntity;
import com.example.nutritrack.viewmodel.FoodViewModel;

import java.util.List;

public class DashboardFragment extends Fragment {

    private TextView tvCaloriesGoal;
    private TextView tvCaloriesConsumed;
    private TextView tvCaloriesRemaining;
    private TextView tvProteinGoal;
    private TextView tvCarbsGoal;
    private TextView tvFatGoal;
    private ProgressBar progressCalories;
    private RecyclerView recyclerViewRecentMeals;
    private FoodEntryAdapter adapter;
    
    private FoodViewModel foodViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        // Initialiser les vues
        tvCaloriesGoal = view.findViewById(R.id.tv_calories_goal);
        tvCaloriesConsumed = view.findViewById(R.id.tv_calories_consumed);
        tvCaloriesRemaining = view.findViewById(R.id.tv_calories_remaining);
        tvProteinGoal = view.findViewById(R.id.tv_protein_goal);
        tvCarbsGoal = view.findViewById(R.id.tv_carbs_goal);
        tvFatGoal = view.findViewById(R.id.tv_fat_goal);
        progressCalories = view.findViewById(R.id.progress_calories);
        recyclerViewRecentMeals = view.findViewById(R.id.recycler_view_recent_meals);
        
        // Configurer le RecyclerView
        recyclerViewRecentMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodEntryAdapter();
        recyclerViewRecentMeals.setAdapter(adapter);
        
        // Configurer le ViewModel
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        
        // Charger les données de l'utilisateur
        loadUserData();
        
        // Observer les calories et macronutriments consommés aujourd'hui
        observeNutritionData();
        
        // Observer les repas récents
        observeRecentMeals();
        
        return view;
    }
    
    private void loadUserData() {
        UserPreferences userPrefs = new UserPreferences(getContext());
        User user = userPrefs.getUser();
        
        if (user != null) {
            int caloriesGoal = user.getCaloriesGoal();
            tvCaloriesGoal.setText(String.valueOf(caloriesGoal));
            tvProteinGoal.setText(String.valueOf(user.getProteinGoal()) + "g");
            tvCarbsGoal.setText(String.valueOf(user.getCarbsGoal()) + "g");
            tvFatGoal.setText(String.valueOf(user.getFatGoal()) + "g");
        } else {
            // Valeurs par défaut si aucun utilisateur n'est configuré
            tvCaloriesGoal.setText("2000");
            tvProteinGoal.setText("150g");
            tvCarbsGoal.setText("200g");
            tvFatGoal.setText("70g");
        }
    }
    
    private void observeNutritionData() {
        // Observer les calories consommées aujourd'hui
        foodViewModel.getTodayTotalCalories().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer calories) {
                int consumedCalories = calories != null ? calories : 0;
                
                // Mettre à jour les calories consommées
                tvCaloriesConsumed.setText(String.valueOf(consumedCalories));
                
                // Calculer les calories restantes
                int goalCalories = Integer.parseInt(tvCaloriesGoal.getText().toString());
                int remainingCalories = goalCalories - consumedCalories;
                tvCaloriesRemaining.setText(String.valueOf(remainingCalories));
                
                // Mettre à jour la barre de progression
                int progressPercentage = Math.min(100, (int) ((float) consumedCalories / goalCalories * 100));
                progressCalories.setProgress(progressPercentage);
            }
        });
        
        // Observer les protéines consommées aujourd'hui
        foodViewModel.getTodayTotalProtein().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float protein) {
                // Ici nous pourrions mettre à jour une barre de progression spécifique pour les protéines
                // Pour l'instant, nous nous contentons d'observer la valeur
            }
        });
        
        // Observer les glucides consommés aujourd'hui
        foodViewModel.getTodayTotalCarbs().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float carbs) {
                // Ici nous pourrions mettre à jour une barre de progression spécifique pour les glucides
            }
        });
        
        // Observer les lipides consommés aujourd'hui
        foodViewModel.getTodayTotalFat().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float fat) {
                // Ici nous pourrions mettre à jour une barre de progression spécifique pour les lipides
            }
        });
    }
    
    private void observeRecentMeals() {
        foodViewModel.getTodayFoodEntries().observe(getViewLifecycleOwner(), new Observer<List<FoodEntryEntity>>() {
            @Override
            public void onChanged(List<FoodEntryEntity> foodEntries) {
                adapter.setFoodEntries(foodEntries);
            }
        });
    }
}