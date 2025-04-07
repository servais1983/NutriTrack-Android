package com.example.nutritrack.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "weight_history",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                     parentColumns = "id",
                     childColumns = "user_id",
                     onDelete = ForeignKey.CASCADE))
public class WeightHistoryEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "weight")
    private float weight;

    @ColumnInfo(name = "date")
    private Date date;

    // Constructeur
    public WeightHistoryEntity(int userId, float weight, Date date) {
        this.userId = userId;
        this.weight = weight;
        this.date = date;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}