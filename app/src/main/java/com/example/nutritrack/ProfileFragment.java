package com.example.nutritrack;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nutritrack.database.AppDatabase;
import com.example.nutritrack.database.entity.UserEntity;
import com.example.nutritrack.database.entity.WeightHistoryEntity;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private EditText etName;
    private EditText etAge;
    private EditText etHeight;
    private EditText etWeight;
    private RadioGroup rgGender;
    private Spinner spActivityLevel;
    private Spinner spGoal;
    private Button btnSave;
    private Button btnSaveWeight;

    private int userId;
    private UserEntity currentUser;
    private ExecutorService executor;
    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupérer l'ID de l'utilisateur depuis les arguments
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("userId", -1);
        } else {
            userId = UserSession.getInstance(getContext()).getUserId();
        }

        // Initialiser l'exécuteur pour les opérations de base de données en arrière-plan
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        etName = view.findViewById(R.id.et_name);
        etAge = view.findViewById(R.id.et_age);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
        rgGender = view.findViewById(R.id.rg_gender);
        spActivityLevel = view.findViewById(R.id.sp_activity_level);
        spGoal = view.findViewById(R.id.sp_goal);
        btnSave = view.findViewById(R.id.btn_save);
        btnSaveWeight = view.findViewById(R.id.btn_save_weight);
        
        // Charger les données de l'utilisateur
        loadUserData();
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
        
        btnSaveWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWeightHistory();
            }
        });
        
        return view;
    }
    
    private void loadUserData() {
        if (userId < 0) {
            Toast.makeText(getContext(), "Erreur: Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
            return;
        }
        
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final UserEntity user = AppDatabase.getInstance(getContext())
                        .userDao().getUserByUsername(UserSession.getInstance(getContext()).getUsername());
                
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            currentUser = user;
                            
                            // Remplir les champs avec les données de l'utilisateur
                            etName.setText(user.getName());
                            etAge.setText(String.valueOf(user.getAge()));
                            etHeight.setText(String.valueOf(user.getHeight()));
                            etWeight.setText(String.valueOf(user.getWeight()));
                            
                            // Définir le genre
                            if ("male".equals(user.getGender())) {
                                rgGender.check(R.id.rb_male);
                            } else {
                                rgGender.check(R.id.rb_female);
                            }
                            
                            // Définir le niveau d'activité
                            ArrayAdapter activityAdapter = (ArrayAdapter) spActivityLevel.getAdapter();
                            int activityPosition = getActivityLevelPosition(user.getActivityLevel());
                            if (activityPosition >= 0) {
                                spActivityLevel.setSelection(activityPosition);
                            }
                            
                            // Définir l'objectif
                            ArrayAdapter goalAdapter = (ArrayAdapter) spGoal.getAdapter();
                            int goalPosition = getGoalPosition(user.getGoal());
                            if (goalPosition >= 0) {
                                spGoal.setSelection(goalPosition);
                            }
                        } else {
                            Toast.makeText(getContext(), "Erreur: Impossible de charger les données de l'utilisateur", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    
    private void saveUserData() {
        // Valider les entrées
        if (!validateInputs()) {
            return;
        }
        
        // Récupérer les valeurs des champs
        final String name = etName.getText().toString().trim();
        final int age = Integer.parseInt(etAge.getText().toString().trim());
        final float height = Float.parseFloat(etHeight.getText().toString().trim());
        final float weight = Float.parseFloat(etWeight.getText().toString().trim());
        
        final String gender = (rgGender.getCheckedRadioButtonId() == R.id.rb_male) ? "male" : "female";
        
        final String activityLevel = getActivityLevelValue(spActivityLevel.getSelectedItemPosition());
        final String goal = getGoalValue(spGoal.getSelectedItemPosition());
        
        // Calculer les besoins caloriques et macronutriments
        final int caloriesGoal = calculateCaloriesGoal(age, height, weight, gender, activityLevel, goal);
        final int proteinGoal = calculateProteinGoal(weight, goal);
        final int fatGoal = calculateFatGoal(caloriesGoal, goal);
        final int carbsGoal = calculateCarbsGoal(caloriesGoal, proteinGoal, fatGoal);
        
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (currentUser != null) {
                    // Mettre à jour les données de l'utilisateur
                    currentUser.setName(name);
                    currentUser.setAge(age);
                    currentUser.setHeight(height);
                    currentUser.setWeight(weight);
                    currentUser.setGender(gender);
                    currentUser.setActivityLevel(activityLevel);
                    currentUser.setGoal(goal);
                    currentUser.setCaloriesGoal(caloriesGoal);
                    currentUser.setProteinGoal(proteinGoal);
                    currentUser.setCarbsGoal(carbsGoal);
                    currentUser.setFatGoal(fatGoal);
                    
                    // Enregistrer dans la base de données
                    AppDatabase.getInstance(getContext()).userDao().update(currentUser);
                    
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), R.string.success_profile_updated, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    
    private void saveWeightHistory() {
        if (!validateWeightInput()) {
            return;
        }
        
        final float weight = Float.parseFloat(etWeight.getText().toString().trim());
        final Date currentDate = new Date();
        
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Créer une nouvelle entrée d'historique de poids
                WeightHistoryEntity weightHistory = new WeightHistoryEntity(userId, weight, currentDate);
                
                // Enregistrer dans la base de données
                long result = AppDatabase.getInstance(getContext()).weightHistoryDao().insert(weightHistory);
                
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result > 0) {
                            Toast.makeText(getContext(), R.string.success_weight_added, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Erreur lors de l'enregistrement du poids", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    
    private boolean validateInputs() {
        // Valider que tous les champs obligatoires sont remplis
        if (etName.getText().toString().trim().isEmpty() ||
            etAge.getText().toString().trim().isEmpty() ||
            etHeight.getText().toString().trim().isEmpty() ||
            etWeight.getText().toString().trim().isEmpty()) {
            
            Toast.makeText(getContext(), R.string.error_required_field, Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Valider que les valeurs numériques sont positives
        try {
            int age = Integer.parseInt(etAge.getText().toString().trim());
            float height = Float.parseFloat(etHeight.getText().toString().trim());
            float weight = Float.parseFloat(etWeight.getText().toString().trim());
            
            if (age <= 0 || height <= 0 || weight <= 0) {
                Toast.makeText(getContext(), "Les valeurs doivent être positives", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Entrez des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    private boolean validateWeightInput() {
        if (etWeight.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer votre poids", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        try {
            float weight = Float.parseFloat(etWeight.getText().toString().trim());
            if (weight <= 0) {
                Toast.makeText(getContext(), "Le poids doit être positif", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Entrez un poids valide", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    // Méthodes utilitaires pour convertir entre les positions des spinners et les valeurs stockées
    private int getActivityLevelPosition(String activityLevel) {
        switch (activityLevel) {
            case "sedentary": return 0;
            case "lightly_active": return 1;
            case "moderately_active": return 2;
            case "very_active": return 3;
            case "extra_active": return 4;
            default: return 0;
        }
    }
    
    private String getActivityLevelValue(int position) {
        switch (position) {
            case 0: return "sedentary";
            case 1: return "lightly_active";
            case 2: return "moderately_active";
            case 3: return "very_active";
            case 4: return "extra_active";
            default: return "sedentary";
        }
    }
    
    private int getGoalPosition(String goal) {
        switch (goal) {
            case "lose_weight": return 0;
            case "maintain_weight": return 1;
            case "gain_muscle": return 2;
            case "gain_weight": return 3;
            default: return 1;
        }
    }
    
    private String getGoalValue(int position) {
        switch (position) {
            case 0: return "lose_weight";
            case 1: return "maintain_weight";
            case 2: return "gain_muscle";
            case 3: return "gain_weight";
            default: return "maintain_weight";
        }
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
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}