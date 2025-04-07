package com.example.nutritrack.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.nutritrack.R;
import com.example.nutritrack.nutrition.FoodItem;

public class AddFoodDialog extends DialogFragment {

    private EditText etFoodName;
    private EditText etCalories;
    private EditText etProtein;
    private EditText etCarbs;
    private EditText etFat;
    
    private AddFoodDialogListener listener;
    
    public interface AddFoodDialogListener {
        void onFoodAdded(FoodItem foodItem);
    }
    
    public void setListener(AddFoodDialogListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        
        // Inflater la vue du dialogue
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_food, null);
        
        // Initialiser les vues
        etFoodName = view.findViewById(R.id.et_food_name);
        etCalories = view.findViewById(R.id.et_calories);
        etProtein = view.findViewById(R.id.et_protein);
        etCarbs = view.findViewById(R.id.et_carbs);
        etFat = view.findViewById(R.id.et_fat);
        
        // Construire le dialogue
        builder.setView(view)
                .setTitle("Ajouter un aliment")
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ne rien faire ici car nous allons surcharger cette méthode
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        
        return builder.create();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // Surcharger le bouton positif pour valider les entrées avant de fermer
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInputs()) {
                        // Créer un nouvel aliment et informer l'écouteur
                        String name = etFoodName.getText().toString().trim();
                        int calories = Integer.parseInt(etCalories.getText().toString().trim());
                        float protein = Float.parseFloat(etProtein.getText().toString().trim());
                        float carbs = Float.parseFloat(etCarbs.getText().toString().trim());
                        float fat = Float.parseFloat(etFat.getText().toString().trim());
                        
                        FoodItem newFood = new FoodItem(name, calories, protein, carbs, fat);
                        
                        if (listener != null) {
                            listener.onFoodAdded(newFood);
                        }
                        
                        // Fermer le dialogue
                        dialog.dismiss();
                    }
                }
            });
        }
    }
    
    private boolean validateInputs() {
        // Valider que tous les champs sont remplis
        if (etFoodName.getText().toString().trim().isEmpty() ||
                etCalories.getText().toString().trim().isEmpty() ||
                etProtein.getText().toString().trim().isEmpty() ||
                etCarbs.getText().toString().trim().isEmpty() ||
                etFat.getText().toString().trim().isEmpty()) {
            
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Valider que les valeurs numériques sont valides
        try {
            int calories = Integer.parseInt(etCalories.getText().toString().trim());
            float protein = Float.parseFloat(etProtein.getText().toString().trim());
            float carbs = Float.parseFloat(etCarbs.getText().toString().trim());
            float fat = Float.parseFloat(etFat.getText().toString().trim());
            
            if (calories < 0 || protein < 0 || carbs < 0 || fat < 0) {
                Toast.makeText(getContext(), "Les valeurs doivent être positives", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Veuillez entrer des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
}