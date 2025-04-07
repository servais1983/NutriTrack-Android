package com.example.nutritrack.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.nutritrack.R;

public class AddFoodDialog extends DialogFragment {

    private EditText etName;
    private EditText etCalories;
    private EditText etProtein;
    private EditText etCarbs;
    private EditText etFat;
    private AddFoodDialogListener listener;

    public interface AddFoodDialogListener {
        void onAddFood(String name, int calories, float protein, float carbs, float fat);
    }

    public void setListener(AddFoodDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_food, null);

        // Initialiser les vues
        etName = view.findViewById(R.id.et_food_name);
        etCalories = view.findViewById(R.id.et_calories);
        etProtein = view.findViewById(R.id.et_protein);
        etCarbs = view.findViewById(R.id.et_carbs);
        etFat = view.findViewById(R.id.et_fat);

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnAdd = view.findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood();
            }
        });

        builder.setView(view);
        builder.setTitle("Ajouter un aliment personnalisé");

        return builder.create();
    }

    private void addFood() {
        // Valider les entrées
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer un nom", Toast.LENGTH_SHORT).show();
            return;
        }

        int calories;
        float protein, carbs, fat;

        try {
            calories = Integer.parseInt(etCalories.getText().toString().trim());
            protein = Float.parseFloat(etProtein.getText().toString().trim());
            carbs = Float.parseFloat(etCarbs.getText().toString().trim());
            fat = Float.parseFloat(etFat.getText().toString().trim());

            if (calories < 0 || protein < 0 || carbs < 0 || fat < 0) {
                Toast.makeText(getContext(), "Les valeurs doivent être positives", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Veuillez entrer des valeurs numériques valides", Toast.LENGTH_SHORT).show();
            return;
        }

        // Appeler le listener
        if (listener != null) {
            listener.onAddFood(name, calories, protein, carbs, fat);
        }

        dismiss();
    }
}