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

    @Query("DELETE FROM food_entries")
    void deleteAll();

    @Query("SELECT * FROM food_entries WHERE id = :id")
    LiveData<FoodEntryEntity> getFoodEntryById(long id);

    @Query("SELECT * FROM food_entries ORDER BY date_time DESC")
    LiveData<List<FoodEntryEntity>> getAllFoodEntries();

    @Query("SELECT * FROM food_entries WHERE date_time >= :startDate AND date_time <= :endDate ORDER BY date_time DESC")
    LiveData<List<FoodEntryEntity>> getFoodEntriesByDateRange(Date startDate, Date endDate);

    @Query("SELECT * FROM food_entries WHERE meal_type = :mealType ORDER BY date_time DESC")
    LiveData<List<FoodEntryEntity>> getFoodEntriesByMealType(String mealType);

    @Query("SELECT SUM(calories) FROM food_entries WHERE date_time >= :startDate AND date_time <= :endDate")
    LiveData<Integer> getTotalCaloriesByDateRange(Date startDate, Date endDate);

    @Query("SELECT SUM(protein) FROM food_entries WHERE date_time >= :startDate AND date_time <= :endDate")
    LiveData<Float> getTotalProteinByDateRange(Date startDate, Date endDate);

    @Query("SELECT SUM(carbs) FROM food_entries WHERE date_time >= :startDate AND date_time <= :endDate")
    LiveData<Float> getTotalCarbsByDateRange(Date startDate, Date endDate);

    @Query("SELECT SUM(fat) FROM food_entries WHERE date_time >= :startDate AND date_time <= :endDate")
    LiveData<Float> getTotalFatByDateRange(Date startDate, Date endDate);
}