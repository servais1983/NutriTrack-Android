package com.example.nutritrack.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "food_entries",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                      parentColumns = "id",
                      childColumns = "user_id",
                      onDelete = ForeignKey.CASCADE))
public class FoodEntryEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "calories")
    private int calories;

    @ColumnInfo(name = "protein")
    private float protein;

    @ColumnInfo(name = "carbs")
    private float carbs;

    @ColumnInfo(name = "fat")
    private float fat;

    @ColumnInfo(name = "quantity")
    private float quantity;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "meal_type")
    private String mealType; // "breakfast", "lunch", "dinner", "snack"

    @ColumnInfo(name = "barcode")
    private String barcode;

    // Constructeur
    public FoodEntryEntity() {
        // Constructeur vide requis par Room
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}