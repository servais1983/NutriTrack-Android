package com.example.nutritrack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nutritrack.database.AppDatabase;
import com.example.nutritrack.database.dao.FoodEntryDao;
import com.example.nutritrack.database.entity.FoodEntryEntity;
import com.example.nutritrack.repository.FoodRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodViewModel extends AndroidViewModel {

    private FoodRepository repository;
    private LiveData<List<FoodEntryEntity>> allFoodEntries;
    private LiveData<List<FoodEntryEntity>> todayFoodEntries;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);
        allFoodEntries = repository.getAllFoodEntries();
        todayFoodEntries = repository.getFoodEntriesByDateRange(getTodayStart(), getTodayEnd());
    }

    public LiveData<List<FoodEntryEntity>> getAllFoodEntries() {
        return allFoodEntries;
    }

    public LiveData<List<FoodEntryEntity>> getTodayFoodEntries() {
        return todayFoodEntries;
    }

    public LiveData<List<FoodEntryEntity>> getFoodEntriesByMealType(String mealType) {
        return repository.getFoodEntriesByMealType(mealType);
    }

    public LiveData<Integer> getTodayTotalCalories() {
        return repository.getTotalCaloriesByDateRange(getTodayStart(), getTodayEnd());
    }

    public LiveData<Float> getTodayTotalProtein() {
        return repository.getTotalProteinByDateRange(getTodayStart(), getTodayEnd());
    }

    public LiveData<Float> getTodayTotalCarbs() {
        return repository.getTotalCarbsByDateRange(getTodayStart(), getTodayEnd());
    }

    public LiveData<Float> getTodayTotalFat() {
        return repository.getTotalFatByDateRange(getTodayStart(), getTodayEnd());
    }

    public void insert(FoodEntryEntity foodEntry) {
        repository.insert(foodEntry);
    }

    public void update(FoodEntryEntity foodEntry) {
        repository.update(foodEntry);
    }

    public void delete(FoodEntryEntity foodEntry) {
        repository.delete(foodEntry);
    }

    private Date getTodayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getTodayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}