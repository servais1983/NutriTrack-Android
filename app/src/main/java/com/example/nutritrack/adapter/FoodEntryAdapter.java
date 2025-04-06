package com.example.nutritrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritrack.R;
import com.example.nutritrack.database.entity.FoodEntryEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodEntryAdapter extends RecyclerView.Adapter<FoodEntryAdapter.FoodEntryViewHolder> {

    private List<FoodEntryEntity> foodEntries = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public FoodEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_entry, parent, false);
        return new FoodEntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodEntryViewHolder holder, int position) {
        FoodEntryEntity currentFoodEntry = foodEntries.get(position);
        
        holder.textViewName.setText(currentFoodEntry.getName());
        holder.textViewCalories.setText(String.valueOf(currentFoodEntry.getCalories()) + " kcal");
        holder.textViewQuantity.setText(String.valueOf(currentFoodEntry.getQuantity()) + "g");
        
        // Format macros
        String macros = String.format(Locale.getDefault(), "P: %.1fg • C: %.1fg • L: %.1fg", 
                currentFoodEntry.getProtein(), 
                currentFoodEntry.getCarbs(), 
                currentFoodEntry.getFat());
        holder.textViewMacros.setText(macros);
        
        // Format date/time
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.textViewDateTime.setText(dateFormat.format(currentFoodEntry.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return foodEntries.size();
    }

    public void setFoodEntries(List<FoodEntryEntity> foodEntries) {
        this.foodEntries = foodEntries;
        notifyDataSetChanged();
    }

    public FoodEntryEntity getFoodEntryAt(int position) {
        return foodEntries.get(position);
    }

    class FoodEntryViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewCalories;
        private TextView textViewQuantity;
        private TextView textViewMacros;
        private TextView textViewDateTime;

        public FoodEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_food_name);
            textViewCalories = itemView.findViewById(R.id.text_view_calories);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            textViewMacros = itemView.findViewById(R.id.text_view_macros);
            textViewDateTime = itemView.findViewById(R.id.text_view_date_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(foodEntries.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(FoodEntryEntity foodEntry);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}