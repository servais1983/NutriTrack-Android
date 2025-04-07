package com.example.nutritrack;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe pour gérer la session utilisateur actuelle (utilisateur connecté)
 */
public class UserSession {

    private static final String PREF_NAME = "UserSessionPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private static UserSession instance;

    private UserSession(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Crée une session pour l'utilisateur connecté
     */
    public void createLoginSession(int userId, String username, String name) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    /**
     * Vérifie si l'utilisateur est connecté
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Récupère l'ID de l'utilisateur connecté
     */
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    /**
     * Récupère le nom d'utilisateur de l'utilisateur connecté
     */
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    /**
     * Récupère le nom complet de l'utilisateur connecté
     */
    public String getName() {
        return prefs.getString(KEY_NAME, null);
    }

    /**
     * Efface les données de session et déconnecte l'utilisateur
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }
}