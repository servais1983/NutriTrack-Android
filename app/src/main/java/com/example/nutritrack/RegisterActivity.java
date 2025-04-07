package com.example.nutritrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nutritrack.database.AppDatabase;
import com.example.nutritrack.database.entity.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etName;
    private EditText etAge;
    private EditText etHeight;
    private EditText etWeight;
    private RadioGroup rgGender;
    private Spinner spActivityLevel;
    private Spinner spGoal;
    private Button btnRegister;
    private TextView tvLogin;

    private ExecutorService executor;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialiser l'exécuteur pour les opérations de base de données en arrière-plan
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        // Initialiser les vues
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        rgGender = findViewById(R.id.rg_gender);
        spActivityLevel = findViewById(R.id.sp_activity_level);
        spGoal = findViewById(R.id.sp_goal);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);

        // Configurer les écouteurs
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void register() {
        // Récupérer les valeurs des champs
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        
        // Validation des champs de base
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Validation des champs numériques
        int age;
        float height;
        float weight;
        try {
            age = Integer.parseInt(etAge.getText().toString().trim());
            height = Float.parseFloat(etHeight.getText().toString().trim());
            weight = Float.parseFloat(etWeight.getText().toString().trim());
            
            if (age <= 0 || height <= 0 || weight <= 0) {
                Toast.makeText(this, "Veuillez entrer des valeurs positives pour l'âge, la taille et le poids", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Récupérer le genre sélectionné
        final String gender = (rgGender.getCheckedRadioButtonId() == R.id.rb_male) ? "male" : "female";
        
        // Récupérer les valeurs des spinners
        final String activityLevel = spActivityLevel.getSelectedItem().toString();
        final String goal = spGoal.getSelectedItem().toString();
        
        // Calculer les objectifs caloriques et macronutriments
        final int caloriesGoal = calculateCaloriesGoal(age, height, weight, gender, activityLevel, goal);
        final int proteinGoal = calculateProteinGoal(weight, goal);
        final int fatGoal = calculateFatGoal(caloriesGoal, goal);
        final int carbsGoal = calculateCarbsGoal(caloriesGoal, proteinGoal, fatGoal);
        
        // Vérifier si le nom d'utilisateur existe déjà
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final UserEntity existingUser = AppDatabase.getInstance(RegisterActivity.this)
                        .userDao().getUserByUsername(username);
                
                if (existingUser != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Ce nom d'utilisateur existe déjà", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                
                // Créer un nouvel utilisateur
                final UserEntity newUser = new UserEntity(username, password, name, age, height, weight,
                        gender, activityLevel, goal, caloriesGoal, proteinGoal, carbsGoal, fatGoal);
                
                // Insérer l'utilisateur dans la base de données
                final long userId = AppDatabase.getInstance(RegisterActivity.this)
                        .userDao().insert(newUser);
                
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (userId > 0) {
                            // Inscription réussie, créer une session et rediriger
                            UserSession.getInstance(RegisterActivity.this)
                                    .createLoginSession((int)userId, username, name);
                            Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        } else {
                            // Échec de l'inscription
                            Toast.makeText(RegisterActivity.this, "Échec de l'inscription, veuillez réessayer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    
    // Calculer BMR et besoins caloriques (même méthode que dans ProfileFragment)
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

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}