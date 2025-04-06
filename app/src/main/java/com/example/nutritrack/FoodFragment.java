package com.example.nutritrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritrack.adapter.FoodEntryAdapter;
import com.example.nutritrack.barcode.BarcodeScannerActivity;
import com.example.nutritrack.database.entity.FoodEntryEntity;
import com.example.nutritrack.dialog.AddFoodDialogFragment;
import com.example.nutritrack.nutrition.NutritionScannerActivity;
import com.example.nutritrack.viewmodel.FoodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodFragment extends Fragment implements AddFoodDialogFragment.AddFoodDialogListener {

    private static final int REQUEST_BARCODE_SCAN = 100;
    private static final int REQUEST_NUTRITION_SCAN = 101;

    private FoodViewModel foodViewModel;
    private RecyclerView recyclerView;
    private FoodEntryAdapter adapter;
    private FloatingActionButton fabAddFood;
    private TextView tvEmptyFoodList;
    private TabLayout tabLayout;
    
    private String currentMealType = "Petit déjeuner";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        
        recyclerView = view.findViewById(R.id.recycler_view_food);
        fabAddFood = view.findViewById(R.id.fab_add_food);
        tvEmptyFoodList = view.findViewById(R.id.tv_empty_food_list);
        tabLayout = view.findViewById(R.id.tab_layout_meals);
        
        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodEntryAdapter();
        recyclerView.setAdapter(adapter);
        
        // Configurer le ViewModel
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        
        // Observer les changements dans la liste d'aliments pour le repas actuel
        updateFoodListForMealType(currentMealType);
        
        // Gérer les clics sur les onglets
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String mealType = tab.getText().toString();
                currentMealType = mealType;
                updateFoodListForMealType(mealType);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Non utilisé
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Non utilisé
            }
        });
        
        // Action du bouton d'ajout
        fabAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodOptions();
            }
        });
        
        // Gérer les clics sur les éléments
        adapter.setOnItemClickListener(new FoodEntryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodEntryEntity foodEntry) {
                // Afficher les détails de l'aliment ou permettre l'édition
                Toast.makeText(getContext(), "Aliment: " + foodEntry.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        
        return view;
    }
    
    private void updateFoodListForMealType(String mealType) {
        foodViewModel.getFoodEntriesByMealType(mealType).observe(getViewLifecycleOwner(), new Observer<List<FoodEntryEntity>>() {
            @Override
            public void onChanged(List<FoodEntryEntity> foodEntries) {
                adapter.setFoodEntries(foodEntries);
                
                // Afficher un message si la liste est vide
                if (foodEntries.isEmpty()) {
                    tvEmptyFoodList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmptyFoodList.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    private void showAddFoodOptions() {
        // Vous pourriez afficher ici un menu contextuel avec les options d'ajout
        // Pour simplifier, nous affichons directement la boîte de dialogue d'ajout
        AddFoodDialogFragment dialogFragment = AddFoodDialogFragment.newInstance();
        dialogFragment.show(getChildFragmentManager(), "AddFoodDialog");
        
        // Alternative: lancer directement les activités de scan
        // startBarcodeScanner();
        // startNutritionScanner();
    }
    
    private void startBarcodeScanner() {
        Intent intent = new Intent(getActivity(), BarcodeScannerActivity.class);
        startActivityForResult(intent, REQUEST_BARCODE_SCAN);
    }
    
    private void startNutritionScanner() {
        Intent intent = new Intent(getActivity(), NutritionScannerActivity.class);
        startActivityForResult(intent, REQUEST_NUTRITION_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_BARCODE_SCAN && data != null) {
                String barcode = data.getStringExtra(BarcodeScannerActivity.EXTRA_BARCODE_VALUE);
                if (barcode != null) {
                    // Dans une application réelle, vous rechercheriez le code-barres dans une base de données
                    // ou une API pour obtenir les informations nutritionnelles
                    Toast.makeText(getContext(), "Code-barres détecté: " + barcode, Toast.LENGTH_LONG).show();
                    
                    // Pour l'exemple, ouvrir directement la boîte de dialogue d'ajout
                    AddFoodDialogFragment dialogFragment = AddFoodDialogFragment.newInstance();
                    dialogFragment.show(getChildFragmentManager(), "AddFoodDialog");
                }
            } else if (requestCode == REQUEST_NUTRITION_SCAN && data != null) {
                HashMap<String, Float> nutritionInfo = 
                        (HashMap<String, Float>) data.getSerializableExtra(NutritionScannerActivity.EXTRA_NUTRITION_INFO);
                
                if (nutritionInfo != null && !nutritionInfo.isEmpty()) {
                    // Remplir le formulaire d'ajout d'aliment avec les informations détectées
                    String foodName = "Aliment scanné";
                    int calories = nutritionInfo.containsKey("calories") ? Math.round(nutritionInfo.get("calories")) : 0;
                    float protein = nutritionInfo.getOrDefault("protein", 0f);
                    float carbs = nutritionInfo.getOrDefault("carbs", 0f);
                    float fat = nutritionInfo.getOrDefault("fat", 0f);
                    
                    AddFoodDialogFragment dialogFragment = 
                            AddFoodDialogFragment.newInstance(foodName, calories, protein, carbs, fat);
                    dialogFragment.show(getChildFragmentManager(), "AddFoodDialog");
                }
            }
        }
    }

    @Override
    public void onFoodAdded(FoodEntryEntity foodEntry) {
        // Définir le type de repas actuel
        foodEntry.setMealType(currentMealType);
        
        // Enregistrer l'aliment dans la base de données
        foodViewModel.insert(foodEntry);
        
        Toast.makeText(getContext(), "Aliment ajouté: " + foodEntry.getName(), Toast.LENGTH_SHORT).show();
    }
}