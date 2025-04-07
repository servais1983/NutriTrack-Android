package com.example.nutritrack.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutritrack.database.entity.FoodEntryEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface FoodEntryDao {

    @Insert
    long insert(FoodEntryEntity foodEntry);

    @Update
    void update(FoodEntryEntity foodEntry);

    @Delete
    void delete(FoodEntryEntity foodEntry);

    @Query("SELECT * FROM food_entries WHERE id = :id")
    LiveData<FoodEntryEntity> getFoodEntryById(int id);

    @Query("SELECT * FROM food_entries WHERE user_id = :userId ORDER BY date DESC")
    LiveData<List<FoodEntryEntity>> getFoodEntriesByUserId(int userId);

    @Query("SELECT * FROM food_entries WHERE user_id = :userId AND date >= :startDate AND date <= :endDate ORDER BY date ASC")
    LiveData<List<FoodEntryEntity>> getFoodEntriesByUserIdAndDateRange(int userId, Date startDate, Date endDate);

    @Query("SELECT * FROM food_entries WHERE user_id = :userId AND date = :date")
    LiveData<List<FoodEntryEntity>> getFoodEntriesByUserIdAndDate(int userId, Date date);

    @Query("SELECT * FROM food_entries WHERE user_id = :userId AND meal_type = :mealType AND date = :date")
    LiveData<List<FoodEntryEntity>> getFoodEntriesByUserIdAndMealTypeAndDate(int userId, String mealType, Date date);

    @Query("SELECT SUM(calories) FROM food_entries WHERE user_id = :userId AND date = :date")
    LiveData<Integer> getTotalCaloriesByUserIdAndDate(int userId, Date date);

    @Query("SELECT SUM(protein) FROM food_entries WHERE user_id = :userId AND date = :date")
    LiveData<Float> getTotalProteinByUserIdAndDate(int userId, Date date);

    @Query("SELECT SUM(carbs) FROM food_entries WHERE user_id = :userId AND date = :date")
    LiveData<Float> getTotalCarbsByUserIdAndDate(int userId, Date date);

    @Query("SELECT SUM(fat) FROM food_entries WHERE user_id = :userId AND date = :date")
    LiveData<Float> getTotalFatByUserIdAndDate(int userId, Date date);
}