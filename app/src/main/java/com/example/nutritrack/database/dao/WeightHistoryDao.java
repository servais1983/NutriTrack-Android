package com.example.nutritrack.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutritrack.database.entity.WeightHistoryEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface WeightHistoryDao {

    @Insert
    long insert(WeightHistoryEntity weightHistory);

    @Update
    void update(WeightHistoryEntity weightHistory);

    @Delete
    void delete(WeightHistoryEntity weightHistory);

    @Query("DELETE FROM weight_history")
    void deleteAll();

    @Query("SELECT * FROM weight_history WHERE id = :id")
    LiveData<WeightHistoryEntity> getWeightHistoryById(long id);

    @Query("SELECT * FROM weight_history ORDER BY date DESC")
    LiveData<List<WeightHistoryEntity>> getAllWeightHistory();

    @Query("SELECT * FROM weight_history WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    LiveData<List<WeightHistoryEntity>> getWeightHistoryByDateRange(Date startDate, Date endDate);

    @Query("SELECT * FROM weight_history ORDER BY date DESC LIMIT 1")
    LiveData<WeightHistoryEntity> getLatestWeight();
}