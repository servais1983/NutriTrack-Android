package com.example.nutritrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProgressFragment extends Fragment {

    private TextView tvCurrentWeight;
    private EditText etNewWeight;
    private Button btnUpdateWeight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        etNewWeight = view.findViewById(R.id.et_new_weight);
        btnUpdateWeight = view.findViewById(R.id.btn_update_weight);
        
        // Charger le poids actuel
        loadCurrentWeight();
        
        btnUpdateWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeight();
            }
        });
        
        return view;
    }
    
    private void loadCurrentWeight() {
        // Dans une application réelle, ces données proviendraient d'une base de données
        UserPreferences userPrefs = new UserPreferences(getContext());
        User user = userPrefs.getUser();
        
        if (user != null) {
            tvCurrentWeight.setText(String.valueOf(user.getWeight()) + " kg");
        }
    }
    
    private void updateWeight() {
        String weightStr = etNewWeight.getText().toString();
        if (!weightStr.isEmpty()) {
            try {
                float newWeight = Float.parseFloat(weightStr);
                
                // Mettre à jour dans la base de données
                UserPreferences userPrefs = new UserPreferences(getContext());
                User user = userPrefs.getUser();
                if (user != null) {
                    user.setWeight(newWeight);
                    userPrefs.saveUser(user);
                    
                    // Enregistrer l'historique du poids
                    saveWeightHistory(newWeight);
                    
                    // Actualiser l'affichage
                    tvCurrentWeight.setText(String.valueOf(newWeight) + " kg");
                    etNewWeight.setText("");
                }
            } catch (NumberFormatException e) {
                // Gérer l'erreur
            }
        }
    }
    
    private void saveWeightHistory(float weight) {
        // À implémenter: enregistrer l'historique du poids avec la date actuelle dans la BD Room
        // Exemple avec une entité WeightHistoryEntity et un DAO WeightHistoryDao
    }
}