package com.example.calendarapp.App;

import android.app.Application;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.TokenManagement.TokenManager;

// Custom Application class to initialize app-wide services like TokenManager
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        API.init(this);
    }
}
