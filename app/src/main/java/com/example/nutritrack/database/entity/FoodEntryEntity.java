package com.example.nutritrack.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "food_entries")
public class FoodEntryEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "quantity")
    private float quantity; // en grammes ou ml

    @ColumnInfo(name = "calories")
    private int calories;

    @ColumnInfo(name = "protein")
    private float protein; // en grammes

    @ColumnInfo(name = "carbs")
    private float carbs; // en grammes

    @ColumnInfo(name = "fat")
    private float fat; // en grammes

    @ColumnInfo(name = "date_time")
    private Date dateTime;

    @ColumnInfo(name = "meal_type")
    private String mealType; // "breakfast", "lunch", "dinner", "snack"

    public FoodEntryEntity(String name, float quantity, int calories, float protein, float carbs, float fat, Date dateTime, String mealType) {
        this.name = name;
        this.quantity = quantity;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.dateTime = dateTime;
        this.mealType = mealType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}