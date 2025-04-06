package com.example.nutritrack;

public class User {
    private int age;
    private float height; // en cm
    private float weight; // en kg
    private String gender; // "male" ou "female"
    private String activityLevel; // "sedentary", "lightly_active", "moderately_active", "very_active", "extra_active"
    private String goal; // "lose_weight", "maintain_weight", "gain_muscle", "gain_weight"
    private int caloriesGoal;
    private int proteinGoal; // en grammes
    private int carbsGoal; // en grammes
    private int fatGoal; // en grammes

    public User(int age, float height, float weight, String gender, String activityLevel, String goal,
                int caloriesGoal, int proteinGoal, int carbsGoal, int fatGoal) {
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