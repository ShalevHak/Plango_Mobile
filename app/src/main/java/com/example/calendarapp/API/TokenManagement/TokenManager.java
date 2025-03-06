package com.example.calendarapp.API.TokenManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static TokenManager instance;
    private static SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_JWT = "jwt_token";

    private TokenManager() {
        // Private constructor to prevent direct instantiation
    }

    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        if (instance == null) {
            instance = new TokenManager();
        }
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public void saveToken(String token) {
        if (sharedPreferences == null) {
            Log.e("TokenManager","sharedPreferences is null");
            return;
        }
        sharedPreferences.edit().putString(KEY_JWT, token).apply();
    }

    public String getToken() {
        if (sharedPreferences == null) {
            Log.e("TokenManager","sharedPreferences is null");
            return "";
        }
        return sharedPreferences.getString(KEY_JWT, null);
    }

    public void clearToken() {
        if (sharedPreferences == null) {
            Log.e("TokenManager","sharedPreferences is null");
            return;
        }
        sharedPreferences.edit().remove(KEY_JWT).apply();
    }
}
