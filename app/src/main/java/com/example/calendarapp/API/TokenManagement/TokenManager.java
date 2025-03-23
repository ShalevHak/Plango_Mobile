package com.example.calendarapp.API.TokenManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static TokenManager instance;
    private static SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_JWT = "jwt_token";
    private static final String KEY_USERID = "user_id";

    private String token;
    private String userID;

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


    public String getToken() {
        if (token == null || token.isEmpty()) {
            if (sharedPreferences == null) {
                Log.e("TokenManager", "sharedPreferences is null");
                return "";
            }
            token = sharedPreferences.getString(KEY_JWT, "");
        }
        return token;
    }

    public String getUserID() {
        if (userID == null || userID.isEmpty()) {
            if (sharedPreferences == null) {
                Log.e("TokenManager", "sharedPreferences is null");
                return "";
            }
            userID = sharedPreferences.getString(KEY_USERID, "");
        }
        return userID;
    }

    public void saveToken(String token) {
        if (sharedPreferences == null) {
            Log.e("TokenManager", "sharedPreferences is null");
            return;
        }
        this.token = token;
        sharedPreferences.edit().putString(KEY_JWT, token).apply();
    }

    public void saveUserID(String userID) {
        if (sharedPreferences == null) {
            Log.e("TokenManager", "sharedPreferences is null");
            return;
        }
        this.userID = userID;
        sharedPreferences.edit().putString(KEY_USERID, userID).apply();
    }

    public void clearToken() {
        if (sharedPreferences == null) {
            Log.e("TokenManager", "sharedPreferences is null");
            return;
        }
        token = null;
        userID = null;
        sharedPreferences.edit().remove(KEY_JWT).remove(KEY_USERID).apply();
    }


}
