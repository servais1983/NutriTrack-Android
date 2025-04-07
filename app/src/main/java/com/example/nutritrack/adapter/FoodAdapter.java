package com.example.nutritrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritrack.R;
import com.example.nutritrack.nutrition.FoodItem;

import java.util.List;
import java.util.Locale;

/**
 * Adaptateur pour afficher une liste d'aliments dans un RecyclerView
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodItems;
    private OnFoodItemClickListener listener;

    /**
     * Interface pour gérer les clics sur les éléments de la liste
     */
    public interface OnFoodItemClickListener {
        void onFoodItemClick(FoodItem foodItem);
    }

    public FoodAdapter(List<FoodItem> foodItems, OnFoodItemClickListener listener) {
        this.foodItems = foodItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.bind(foodItem, listener);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    /**
     * ViewHolder pour les éléments de la liste d'aliments
     */
    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvCalories;
        private TextView tvNutrients;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_food_name);
            tvCalories = itemView.findViewById(R.id.tv_food_calories);
            tvNutrients = itemView.findViewById(R.id.tv_food_nutrients);
        }

        public void bind(final FoodItem foodItem, final OnFoodItemClickListener listener) {
            tvName.setText(foodItem.getName());
            tvCalories.setText(String.format(Locale.getDefault(), "%d kcal", foodItem.getCalories()));
            tvNutrients.setText(String.format(Locale.getDefault(), 
                    "P: %.1fg | C: %.1fg | L: %.1fg", 
                    foodItem.getProtein(), foodItem.getCarbs(), foodItem.getFat()));

            // Définir le gestionnaire de clic
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFoodItemClick(foodItem);
                }
            });
        }
    }
}
