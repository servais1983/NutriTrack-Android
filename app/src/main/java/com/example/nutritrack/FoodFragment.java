package com.example.nutritrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FoodFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddFood;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        
        recyclerView = view.findViewById(R.id.recycler_view_food);
        fabAddFood = view.findViewById(R.id.fab_add_food);
        
        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Adapter à implémenter
        
        // Action du bouton d'ajout
        fabAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir la boîte de dialogue d'ajout d'aliment
                showAddFoodDialog();
            }
        });
        
        return view;
    }
    
    private void showAddFoodDialog() {
        // À implémenter: boîte de dialogue pour ajouter un aliment manuellement
        // Permettra à l'utilisateur de saisir:
        // - Nom de l'aliment
        // - Quantité (en grammes ou ml)
        // - Type de repas (petit déjeuner, déjeuner, dîner, collation)
        // - Valeurs nutritionnelles (calories, protéines, glucides, lipides)
    }
}