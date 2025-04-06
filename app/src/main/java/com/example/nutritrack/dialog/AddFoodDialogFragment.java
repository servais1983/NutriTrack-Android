package com.example.nutritrack.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.nutritrack.R;
import com.example.nutritrack.database.entity.FoodEntryEntity;

import java.util.Date;

public class AddFoodDialogFragment extends DialogFragment {

    private EditText etFoodName;
    private EditText etQuantity;
    private EditText etCalories;
    private EditText etProtein;
    private EditText etCarbs;
    private EditText etFat;
    private Spinner spMealType;
    
    private AddFoodDialogListener listener;

    public static AddFoodDialogFragment newInstance() {
        return new AddFoodDialogFragment();
    }

    public static AddFoodDialogFragment newInstance(String foodName, int calories, float protein, float carbs, float fat) {
        AddFoodDialogFragment fragment = new AddFoodDialogFragment();
        Bundle args = new Bundle();
        args.putString("foodName", foodName);
        args.putInt("calories", calories);
        args.putFloat("protein", protein);
        args.putFloat("carbs", carbs);
        args.putFloat("fat", fat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddFoodDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            try {
                listener = (AddFoodDialogListener) context;
            } catch (ClassCastException ex) {
                throw new ClassCastException(context.toString() + " doit implémenter AddFoodDialogListener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_food, null);
        
        etFoodName = view.findViewById(R.id.et_food_name);
        etQuantity = view.findViewById(R.id.et_quantity);
        etCalories = view.findViewById(R.id.et_calories);
        etProtein = view.findViewById(R.id.et_protein);
        etCarbs = view.findViewById(R.id.et_carbs);
        etFat = view.findViewById(R.id.et_fat);
        spMealType = view.findViewById(R.id.sp_meal_type);
        
        // Configurer le spinner avec les types de repas
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMealType.setAdapter(adapter);
        
        // Préremplir avec les données si disponibles
        if (getArguments() != null) {
            String foodName = getArguments().getString("foodName", "");
            int calories = getArguments().getInt("calories", 0);
            float protein = getArguments().getFloat("protein", 0);
            float carbs = getArguments().getFloat("carbs", 0);
            float fat = getArguments().getFloat("fat", 0);
            
            etFoodName.setText(foodName);
            etCalories.setText(String.valueOf(calories));
            etProtein.setText(String.valueOf(protein));
            etCarbs.setText(String.valueOf(carbs));
            etFat.setText(String.valueOf(fat));
            etQuantity.setText("100"); // Par défaut, 100g
        }
        
        builder.setView(view)
                .setTitle("Ajouter un aliment")
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (validateInputs()) {
                            String foodName = etFoodName.getText().toString();
                            float quantity = Float.parseFloat(etQuantity.getText().toString());
                            int calories = Integer.parseInt(etCalories.getText().toString());
                            float protein = Float.parseFloat(etProtein.getText().toString());
                            float carbs = Float.parseFloat(etCarbs.getText().toString());
                            float fat = Float.parseFloat(etFat.getText().toString());
                            String mealType = spMealType.getSelectedItem().toString();
                            
                            FoodEntryEntity foodEntry = new FoodEntryEntity(
                                    foodName,
                                    quantity,
                                    calories,
                                    protein,
                                    carbs,
                                    fat,
                                    new Date(),
                                    mealType
                            );
                            
                            listener.onFoodAdded(foodEntry);
                        }
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
    
    private boolean validateInputs() {
        // Vérifier que tous les champs sont remplis
        if (etFoodName.getText().toString().isEmpty() ||
                etQuantity.getText().toString().isEmpty() ||
                etCalories.getText().toString().isEmpty() ||
                etProtein.getText().toString().isEmpty() ||
                etCarbs.getText().toString().isEmpty() ||
                etFat.getText().toString().isEmpty()) {
            return false;
        }
        
        try {
            // Vérifier que les champs numériques contiennent des nombres valides
            Float.parseFloat(etQuantity.getText().toString());
            Integer.parseInt(etCalories.getText().toString());
            Float.parseFloat(etProtein.getText().toString());
            Float.parseFloat(etCarbs.getText().toString());
            Float.parseFloat(etFat.getText().toString());
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public interface AddFoodDialogListener {
        void onFoodAdded(FoodEntryEntity foodEntry);
    }
}