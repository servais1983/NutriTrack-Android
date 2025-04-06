package com.example.nutritrack.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "weight_history")
public class WeightHistoryEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "weight")
    private float weight; // en kg

    @ColumnInfo(name = "date")
    private Date date;

    public WeightHistoryEntity(float weight, Date date) {
        this.weight = weight;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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