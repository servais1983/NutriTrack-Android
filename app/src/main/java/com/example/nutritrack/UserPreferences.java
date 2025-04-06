package com.example.nutritrack;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class UserPreferences {

    private static final String PREFS_NAME = "NutriTrackPrefs";
    private static final String KEY_USER = "user";
    
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER);
        editor.apply();
    }
}