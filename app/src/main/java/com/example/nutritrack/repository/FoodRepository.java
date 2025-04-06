package com.example.nutritrack.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.nutritrack.database.AppDatabase;
import com.example.nutritrack.database.dao.FoodEntryDao;
import com.example.nutritrack.database.entity.FoodEntryEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodRepository {

    private FoodEntryDao foodEntryDao;
    private LiveData<List<FoodEntryEntity>> allFoodEntries;
    private ExecutorService executorService;

    public FoodRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        foodEntryDao = database.foodEntryDao();
        allFoodEntries = foodEntryDao.getAllFoodEntries();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<FoodEntryEntity>> getAllFoodEntries() {
        return allFoodEntries;
    }

    public LiveData<List<FoodEntryEntity>> getFoodEntriesByDateRange(Date startDate, Date endDate) {
        return foodEntryDao.getFoodEntriesByDateRange(startDate, endDate);
    }

    public LiveData<List<FoodEntryEntity>> getFoodEntriesByMealType(String mealType) {
        return foodEntryDao.getFoodEntriesByMealType(mealType);
    }

    public LiveData<Integer> getTotalCaloriesByDateRange(Date startDate, Date endDate) {
        return foodEntryDao.getTotalCaloriesByDateRange(startDate, endDate);
    }

    public LiveData<Float> getTotalProteinByDateRange(Date startDate, Date endDate) {
        return foodEntryDao.getTotalProteinByDateRange(startDate, endDate);
    }

    public LiveData<Float> getTotalCarbsByDateRange(Date startDate, Date endDate) {
        return foodEntryDao.getTotalCarbsByDateRange(startDate, endDate);
    }

    public LiveData<Float> getTotalFatByDateRange(Date startDate, Date endDate) {
        return foodEntryDao.getTotalFatByDateRange(startDate, endDate);
    }

    public void insert(FoodEntryEntity foodEntry) {
        executorService.execute(() -> {
            foodEntryDao.insert(foodEntry);
        });
    }

    public void update(FoodEntryEntity foodEntry) {
        executorService.execute(() -> {
            foodEntryDao.update(foodEntry);
        });
    }

    public void delete(FoodEntryEntity foodEntry) {
        executorService.execute(() -> {
            foodEntryDao.delete(foodEntry);
        });
    }

    public void deleteAll() {
        executorService.execute(() -> {
            foodEntryDao.deleteAll();
        });
    }
}