package com.example.calendarapp.API.TokenManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

// Singleton class for managing token-related SharedPreferences operations
// Initialized once via App context to prevent memory leaks and ensure global access
public class TokenManager {
    private static TokenManager instance;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_JWT = "jwt_token";
    private static final String KEY_USERID = "user_id";

    private String _token;
    private String _userID;

    private TokenManager(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        // Private constructor to prevent direct instantiation
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TokenManager is not initialized. Call init(context) first.");
        }
        return instance;
    }


    public String getToken() {
        if (_token == null || _token.isEmpty()) {
            if (sharedPreferences == null) {
                Log.e("TokenManager", "sharedPreferences is null");
                return "";
            }
            _token = sharedPreferences.getString(KEY_JWT, "");
        }
        return _token;
    }

    public String getUserID() {
        if (_userID == null || _userID.isEmpty()) {
            if (sharedPreferences == null) {
                Log.e("TokenManager", "sharedPreferences is null");
                return "";
            }
            _userID = sharedPreferences.getString(KEY_USERID, "");
        }
        return _userID;
    }

    public void saveToken(String token) {
        if (sharedPreferences == null) {
            Log.e("TokenManager", "sharedPreferences is null");
            return;
        }
        _token = token;
        sharedPreferences.edit().putString(KEY_JWT, token).apply();
        Log.i("TokenManager", "New token saved");
    }

    public void saveUserID(String userID) {
        userID = userID.replace("\"", "");
        if (sharedPreferences == null) {
            Log.e("TokenManager", "sharedPreferences is null");
            return;
        }
        _userID = userID;
        sharedPreferences.edit().putString(KEY_USERID, userID).apply();
        Log.i("TokenManager", "New UserID saved: " + _userID);

    }

    public void clearToken() {
        if (sharedPreferences == null) {
            Log.e("TokenManager", "sharedPreferences is null");
            return;
        }
        _token = null;
        _userID = null;
        sharedPreferences.edit().remove(KEY_JWT).remove(KEY_USERID).apply();
    }


}
