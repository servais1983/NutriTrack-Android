package com.example.nutritrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private TextView tvCaloriesGoal;
    private TextView tvCaloriesConsumed;
    private TextView tvCaloriesRemaining;
    private TextView tvProteinGoal;
    private TextView tvCarbsGoal;
    private TextView tvFatGoal;

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
        
        // Charger les données de l'utilisateur
        loadUserData();
        
        return view;
    }
    
    private void loadUserData() {
        // Dans une application réelle, ces données proviendraient d'une base de données
        UserPreferences userPrefs = new UserPreferences(getContext());
        User user = userPrefs.getUser();
        
        if (user != null) {
            int caloriesGoal = user.getCaloriesGoal();
            int caloriesConsumed = 0; // À récupérer de la base de données
            int caloriesRemaining = caloriesGoal - caloriesConsumed;
            
            tvCaloriesGoal.setText(String.valueOf(caloriesGoal));
            tvCaloriesConsumed.setText(String.valueOf(caloriesConsumed));
            tvCaloriesRemaining.setText(String.valueOf(caloriesRemaining));
            
            tvProteinGoal.setText(String.valueOf(user.getProteinGoal()) + "g");
            tvCarbsGoal.setText(String.valueOf(user.getCarbsGoal()) + "g");
            tvFatGoal.setText(String.valueOf(user.getFatGoal()) + "g");
        }
    }
}