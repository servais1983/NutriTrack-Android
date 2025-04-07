package com.example.nutritrack.dialog;

import android.app.Dialog;
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

import java.util.Locale;

/**
 * Dialogue pour ajouter un aliment au journal
 */
public class AddFoodDialog extends DialogFragment {

    private EditText etName;
    private EditText etCalories;
    private EditText etProtein;
    private EditText etCarbs;
    private EditText etFat;
    private EditText etQuantity;
    private Spinner spMealType;

    private AddFoodDialogListener listener;

    /**
     * Interface pour notifier la création d'un aliment
     */
    public interface AddFoodDialogListener {
        void onFoodAdded(String name, int calories, float protein, float carbs, float fat, float quantity, String mealType);
    }

    public void setListener(AddFoodDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_food, null);

        // Initialiser les vues
        etName = view.findViewById(R.id.et_food_name);
        etCalories = view.findViewById(R.id.et_calories);
        etProtein = view.findViewById(R.id.et_protein);
        etCarbs = view.findViewById(R.id.et_carbs);
        etFat = view.findViewById(R.id.et_fat);
        etQuantity = view.findViewById(R.id.et_quantity);
        spMealType = view.findViewById(R.id.sp_meal_type);

        // Configurer le spinner avec les types de repas
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMealType.setAdapter(adapter);

        // Remplir les champs si des données sont fournies (aliment existant)
        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name", "");
            int calories = args.getInt("calories", 0);
            float protein = args.getFloat("protein", 0);
            float carbs = args.getFloat("carbs", 0);
            float fat = args.getFloat("fat", 0);

            etName.setText(name);
            etCalories.setText(String.format(Locale.getDefault(), "%d", calories));
            etProtein.setText(String.format(Locale.getDefault(), "%.1f", protein));
            etCarbs.setText(String.format(Locale.getDefault(), "%.1f", carbs));
            etFat.setText(String.format(Locale.getDefault(), "%.1f", fat));
            etQuantity.setText("100"); // Quantité par défaut (100g/ml)
        }

        builder.setView(view)
                .setTitle(args != null ? "Ajouter au journal" : "Créer un aliment")
                .setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addFood();
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

    private void addFood() {
        if (listener == null) {
            return;
        }

        // Récupérer et valider les données
        String name = etName.getText().toString().trim();
        String caloriesStr = etCalories.getText().toString().trim();
        String proteinStr = etProtein.getText().toString().trim();
        String carbsStr = etCarbs.getText().toString().trim();
        String fatStr = etFat.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String mealType = spMealType.getSelectedItem().toString();

        if (name.isEmpty() || caloriesStr.isEmpty() || proteinStr.isEmpty() ||
                carbsStr.isEmpty() || fatStr.isEmpty() || quantityStr.isEmpty()) {
            return; // Les champs ne sont pas tous remplis
        }

        try {
            int calories = Integer.parseInt(caloriesStr);
            float protein = Float.parseFloat(proteinStr);
            float carbs = Float.parseFloat(carbsStr);
            float fat = Float.parseFloat(fatStr);
            float quantity = Float.parseFloat(quantityStr);

            listener.onFoodAdded(name, calories, protein, carbs, fat, quantity, mealType);
        } catch (NumberFormatException e) {
            // En cas d'erreur de conversion, on ignore simplement la demande
        }
    }
}
