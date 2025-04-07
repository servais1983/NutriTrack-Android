package com.example.nutritrack.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "username")
    private String username;
    
    @ColumnInfo(name = "password")
    private String password; // À stocker de manière sécurisée avec hachage
    
    @ColumnInfo(name = "name")
    private String name;
    
    @ColumnInfo(name = "age")
    private int age;
    
    @ColumnInfo(name = "height")
    private float height;
    
    @ColumnInfo(name = "weight")
    private float weight;
    
    @ColumnInfo(name = "gender")
    private String gender;
    
    @ColumnInfo(name = "activity_level")
    private String activityLevel;
    
    @ColumnInfo(name = "goal")
    private String goal;
    
    @ColumnInfo(name = "calories_goal")
    private int caloriesGoal;
    
    @ColumnInfo(name = "protein_goal")
    private int proteinGoal;
    
    @ColumnInfo(name = "carbs_goal")
    private int carbsGoal;
    
    @ColumnInfo(name = "fat_goal")
    private int fatGoal;

    // Constructeur
    public UserEntity(String username, String password, String name, int age, float height, float weight,
                     String gender, String activityLevel, String goal, int caloriesGoal,
                     int proteinGoal, int carbsGoal, int fatGoal) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.caloriesGoal = caloriesGoal;
        this.proteinGoal = proteinGoal;
        this.carbsGoal = carbsGoal;
        this.fatGoal = fatGoal;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public int getCaloriesGoal() {
        return caloriesGoal;
    }

    public void setCaloriesGoal(int caloriesGoal) {
        this.caloriesGoal = caloriesGoal;
    }

    public int getProteinGoal() {
        return proteinGoal;
    }

    public void setProteinGoal(int proteinGoal) {
        this.proteinGoal = proteinGoal;
    }

    public int getCarbsGoal() {
        return carbsGoal;
    }

    public void setCarbsGoal(int carbsGoal) {
        this.carbsGoal = carbsGoal;
    }

    public int getFatGoal() {
        return fatGoal;
    }

    public void setFatGoal(int fatGoal) {
        this.fatGoal = fatGoal;
    }
}