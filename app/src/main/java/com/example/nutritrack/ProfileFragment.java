package com.example.nutritrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText etAge;
    private EditText etHeight;
    private EditText etWeight;
    private RadioGroup rgGender;
    private Spinner spActivityLevel;
    private Spinner spGoal;
    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        etAge = view.findViewById(R.id.et_age);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
        rgGender = view.findViewById(R.id.rg_gender);
        spActivityLevel = view.findViewById(R.id.sp_activity_level);
        spGoal = view.findViewById(R.id.sp_goal);
        btnSave = view.findViewById(R.id.btn_save);
        
        // Charger les données de l'utilisateur
        loadUserData();
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
        
        return view;
    }
    
    private void loadUserData() {
        // Dans une application réelle, ces données proviendraient d'une base de données
        UserPreferences userPrefs = new UserPreferences(getContext());
        User user = userPrefs.getUser();
        
        if (user != null) {
            etAge.setText(String.valueOf(user.getAge()));
            etHeight.setText(String.valueOf(user.getHeight()));
            etWeight.setText(String.valueOf(user.getWeight()));
            
            if (user.getGender().equals("male")) {
                rgGender.check(R.id.rb_male);
            } else {
                rgGender.check(R.id.rb_female);
            }
            
            // Définir les sélections pour les spinners
            // (code à implémenter selon la structure des adaptateurs)
        }
    }
    
    private void saveUserData() {
        // Valider les entrées
        if (validateInputs()) {
            int age = Integer.parseInt(etAge.getText().toString());
            float height = Float.parseFloat(etHeight.getText().toString());
            float weight = Float.parseFloat(etWeight.getText().toString());
            
            String gender = (rgGender.getCheckedRadioButtonId() == R.id.rb_male) ? "male" : "female";
            
            String activityLevel = spActivityLevel.getSelectedItem().toString();
            String goal = spGoal.getSelectedItem().toString();
            
            // Calculer les besoins caloriques
            int caloriesGoal = calculateCaloriesGoal(age, height, weight, gender, activityLevel, goal);
            
            // Calculer les macronutriments
            int proteinGoal = calculateProteinGoal(weight, goal);
            int carbsGoal = calculateCarbsGoal(caloriesGoal, proteinGoal, calculateFatGoal(caloriesGoal, goal));
            int fatGoal = calculateFatGoal(caloriesGoal, goal);
            
            // Sauvegarder dans les préférences utilisateur
            User user = new User(age, height, weight, gender, activityLevel, goal, caloriesGoal, proteinGoal, carbsGoal, fatGoal);
            UserPreferences userPrefs = new UserPreferences(getContext());
            userPrefs.saveUser(user);
        }
    }
    
    private boolean validateInputs() {
        // À implémenter: validation des entrées utilisateur
        return true;
    }
    
    private int calculateCaloriesGoal(int age, float height, float weight, String gender, String activityLevel, String goal) {
        // Calculer BMR selon la formule de Mifflin-St Jeor
        float bmr;
        if (gender.equals("male")) {
            bmr = 10 * weight + 6.25f * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25f * height - 5 * age - 161;
        }
        
        // Multiplier par le facteur d'activité
        float tdee = bmr;
        switch (activityLevel) {
            case "sedentary":
                tdee *= 1.2f;
                break;
            case "lightly_active":
                tdee *= 1.375f;
                break;
            case "moderately_active":
                tdee *= 1.55f;
                break;
            case "very_active":
                tdee *= 1.725f;
                break;
            case "extra_active":
                tdee *= 1.9f;
                break;
        }
        
        // Ajuster en fonction de l'objectif
        switch (goal) {
            case "lose_weight":
                tdee *= 0.8f; // Déficit de 20%
                break;
            case "maintain_weight":
                // Pas de changement
                break;
            case "gain_muscle":
                tdee *= 1.1f; // Surplus de 10%
                break;
            case "gain_weight":
                tdee *= 1.2f; // Surplus de 20%
                break;
        }
        
        return Math.round(tdee);
    }
    
    private int calculateProteinGoal(float weight, String goal) {
        float proteinPerKg;
        
        switch (goal) {
            case "lose_weight":
                proteinPerKg = 2.0f; // Plus de protéines pour préserver la masse musculaire
                break;
            case "gain_muscle":
                proteinPerKg = 2.2f; // Protéines élevées pour la récupération musculaire
                break;
            case "gain_weight":
                proteinPerKg = 1.8f;
                break;
            default: // maintain_weight
                proteinPerKg = 1.6f;
                break;
        }
        
        return Math.round(weight * proteinPerKg);
    }
    
    private int calculateFatGoal(int caloriesGoal, String goal) {
        float fatPercentage;
        
        switch (goal) {
            case "lose_weight":
                fatPercentage = 0.25f; // 25% des calories proviennent des graisses
                break;
            case "gain_muscle":
                fatPercentage = 0.25f;
                break;
            case "gain_weight":
                fatPercentage = 0.3f; // 30% des calories proviennent des graisses
                break;
            default: // maintain_weight
                fatPercentage = 0.3f;
                break;
        }
        
        return Math.round((caloriesGoal * fatPercentage) / 9); // 9 calories par gramme de graisse
    }
    
    private int calculateCarbsGoal(int caloriesGoal, int proteinGoal, int fatGoal) {
        // Les calories restantes après protéines et graisses vont aux glucides
        int proteinCalories = proteinGoal * 4; // 4 calories par gramme de protéine
        int fatCalories = fatGoal * 9; // 9 calories par gramme de graisse
        int carbsCalories = caloriesGoal - proteinCalories - fatCalories;
        
        return Math.round(carbsCalories / 4); // 4 calories par gramme de glucides
    }
}