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

    @Query("SELECT * FROM weight_history WHERE id = :id")
    LiveData<WeightHistoryEntity> getWeightHistoryById(int id);

    @Query("SELECT * FROM weight_history WHERE user_id = :userId ORDER BY date ASC")
    LiveData<List<WeightHistoryEntity>> getWeightHistoryByUserId(int userId);

    @Query("SELECT * FROM weight_history WHERE user_id = :userId AND date >= :startDate AND date <= :endDate ORDER BY date ASC")
    LiveData<List<WeightHistoryEntity>> getWeightHistoryByUserIdAndDateRange(int userId, Date startDate, Date endDate);

    @Query("SELECT * FROM weight_history WHERE user_id = :userId ORDER BY date DESC LIMIT 1")
    LiveData<WeightHistoryEntity> getLatestWeightByUserId(int userId);
}