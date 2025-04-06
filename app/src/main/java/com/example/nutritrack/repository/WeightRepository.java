package com.example.nutritrack.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.nutritrack.database.AppDatabase;
import com.example.nutritrack.database.dao.WeightHistoryDao;
import com.example.nutritrack.database.entity.WeightHistoryEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeightRepository {

    private WeightHistoryDao weightHistoryDao;
    private LiveData<List<WeightHistoryEntity>> allWeightHistory;
    private LiveData<WeightHistoryEntity> latestWeight;
    private ExecutorService executorService;

    public WeightRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        weightHistoryDao = database.weightHistoryDao();
        allWeightHistory = weightHistoryDao.getAllWeightHistory();
        latestWeight = weightHistoryDao.getLatestWeight();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<WeightHistoryEntity>> getAllWeightHistory() {
        return allWeightHistory;
    }

    public LiveData<WeightHistoryEntity> getLatestWeight() {
        return latestWeight;
    }

    public LiveData<List<WeightHistoryEntity>> getWeightHistoryByDateRange(Date startDate, Date endDate) {
        return weightHistoryDao.getWeightHistoryByDateRange(startDate, endDate);
    }

    public void insert(WeightHistoryEntity weightHistory) {
        executorService.execute(() -> {
            weightHistoryDao.insert(weightHistory);
        });
    }

    public void update(WeightHistoryEntity weightHistory) {
        executorService.execute(() -> {
            weightHistoryDao.update(weightHistory);
        });
    }

    public void delete(WeightHistoryEntity weightHistory) {
        executorService.execute(() -> {
            weightHistoryDao.delete(weightHistory);
        });
    }

    public void deleteAll() {
        executorService.execute(() -> {
            weightHistoryDao.deleteAll();
        });
    }
}