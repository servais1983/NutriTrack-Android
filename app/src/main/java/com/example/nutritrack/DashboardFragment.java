package com.example.nutritrack;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.nutritrack.database.AppDatabase;
import com.example.nutritrack.database.entity.FoodEntryEntity;
import com.example.nutritrack.database.entity.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardFragment extends Fragment {

    private TextView tvDate;
    private TextView tvCaloriesRemaining;
    private TextView tvTotalCalories;
    private TextView tvTotalProtein;
    private TextView tvTotalCarbs;
    private TextView tvTotalFat;
    private ProgressBar pbCalories;
    private ProgressBar pbProtein;
    private ProgressBar pbCarbs;
    private ProgressBar pbFat;
    private Button btnAddFood;

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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialiser les vues
        tvDate = view.findViewById(R.id.tv_date);
        tvCaloriesRemaining = view.findViewById(R.id.tv_calories_remaining);
        tvTotalCalories = view.findViewById(R.id.tv_total_calories);
        tvTotalProtein = view.findViewById(R.id.tv_total_protein);
        tvTotalCarbs = view.findViewById(R.id.tv_total_carbs);
        tvTotalFat = view.findViewById(R.id.tv_total_fat);
        pbCalories = view.findViewById(R.id.pb_calories);
        pbProtein = view.findViewById(R.id.pb_protein);
        pbCarbs = view.findViewById(R.id.pb_carbs);
        pbFat = view.findViewById(R.id.pb_fat);
        btnAddFood = view.findViewById(R.id.btn_add_food);

        // Définir la date du jour
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());
        tvDate.setText(dateFormat.format(new Date()));

        // Charger les données nutritionnelles
        loadUserData();
        loadNutritionData();

        // Configurer le bouton d'ajout d'aliment
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Naviguer vers le fragment de recherche d'aliments
                navigateToFoodFragment();
            }
        });

        return view;
    }

    private void loadUserData() {
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
                            updateGoalsUI();
                        }
                    }
                });
            }
        });
    }

    private void loadNutritionData() {
        Date today = new Date(); // Date du jour

        // Observer les entrées alimentaires du jour
        AppDatabase.getInstance(getContext()).foodEntryDao()
                .getFoodEntriesByUserIdAndDate(userId, today)
                .observe(getViewLifecycleOwner(), new Observer<List<FoodEntryEntity>>() {
                    @Override
                    public void onChanged(List<FoodEntryEntity> foodEntries) {
                        updateNutritionUI(foodEntries);
                    }
                });
    }

    private void updateGoalsUI() {
        if (currentUser != null) {
            // Mettre à jour les barres de progression avec les objectifs
            pbCalories.setMax(currentUser.getCaloriesGoal());
            pbProtein.setMax(currentUser.getProteinGoal());
            pbCarbs.setMax(currentUser.getCarbsGoal());
            pbFat.setMax(currentUser.getFatGoal());
        }
    }

    private void updateNutritionUI(List<FoodEntryEntity> foodEntries) {
        if (currentUser == null || foodEntries == null) {
            return;
        }

        // Calculer les totaux nutritionnels
        int totalCalories = 0;
        float totalProtein = 0;
        float totalCarbs = 0;
        float totalFat = 0;

        for (FoodEntryEntity entry : foodEntries) {
            totalCalories += entry.getCalories();
            totalProtein += entry.getProtein();
            totalCarbs += entry.getCarbs();
            totalFat += entry.getFat();
        }

        // Mettre à jour les barres de progression
        pbCalories.setProgress(totalCalories);
        pbProtein.setProgress(Math.round(totalProtein));
        pbCarbs.setProgress(Math.round(totalCarbs));
        pbFat.setProgress(Math.round(totalFat));

        // Mettre à jour les textes
        tvTotalCalories.setText(String.format(Locale.getDefault(), "%d / %d kcal", 
                totalCalories, currentUser.getCaloriesGoal()));
        tvTotalProtein.setText(String.format(Locale.getDefault(), "%.1f / %d g", 
                totalProtein, currentUser.getProteinGoal()));
        tvTotalCarbs.setText(String.format(Locale.getDefault(), "%.1f / %d g", 
                totalCarbs, currentUser.getCarbsGoal()));
        tvTotalFat.setText(String.format(Locale.getDefault(), "%.1f / %d g", 
                totalFat, currentUser.getFatGoal()));

        // Calculer les calories restantes
        int caloriesRemaining = currentUser.getCaloriesGoal() - totalCalories;
        tvCaloriesRemaining.setText(String.format(Locale.getDefault(), "%d", caloriesRemaining));

        // Colorer selon le nombre de calories restantes
        if (caloriesRemaining < 0) {
            tvCaloriesRemaining.setTextColor(Color.RED);
        } else {
            tvCaloriesRemaining.setTextColor(Color.GREEN);
        }
    }

    private void navigateToFoodFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Préparer les arguments pour le FoodFragment
        FoodFragment foodFragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        foodFragment.setArguments(args);

        // Remplacer le fragment actuel et ajouter à la pile de retour arrière
        transaction.replace(R.id.fragment_container, foodFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}