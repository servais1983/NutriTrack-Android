package com.example.nutritrack.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nutritrack.database.entity.WeightHistoryEntity;
import com.example.nutritrack.repository.WeightRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeightViewModel extends AndroidViewModel {

    private WeightRepository repository;
    private LiveData<List<WeightHistoryEntity>> allWeightHistory;
    private LiveData<WeightHistoryEntity> latestWeight;

    public WeightViewModel(@NonNull Application application) {
        super(application);
        repository = new WeightRepository(application);
        allWeightHistory = repository.getAllWeightHistory();
        latestWeight = repository.getLatestWeight();
    }

    public LiveData<List<WeightHistoryEntity>> getAllWeightHistory() {
        return allWeightHistory;
    }

    public LiveData<WeightHistoryEntity> getLatestWeight() {
        return latestWeight;
    }

    public LiveData<List<WeightHistoryEntity>> getWeightHistoryByDateRange(Date startDate, Date endDate) {
        return repository.getWeightHistoryByDateRange(startDate, endDate);
    }

    public LiveData<List<WeightHistoryEntity>> getLastMonthWeightHistory() {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();
        
        return getWeightHistoryByDateRange(startDate, endDate);
    }

    public void insert(WeightHistoryEntity weightHistory) {
        repository.insert(weightHistory);
    }

    public void update(WeightHistoryEntity weightHistory) {
        repository.update(weightHistory);
    }

    public void delete(WeightHistoryEntity weightHistory) {
        repository.delete(weightHistory);
    }
}