package com.example.calendarapp.Managers;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.User;

import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

public class UsersManager {
    public static UsersManager usersManager;
    private UsersManager(){

    }

    public static UsersManager getInstance(){
        if (usersManager == null) {
            Log.i("CalendarService","Init CalendarService");
            synchronized (CalendarsManager.class) { // Thread-safe
                if (usersManager == null) {
                    usersManager = new UsersManager();
                }
            }
        }
        return usersManager;
    }

    public CompletableFuture<User> searchUserByEmail(String email) {
        CompletableFuture<User> future = new CompletableFuture<>();
        API.api().usersService.searchUserByEmail(email)
                .thenAccept(res -> {
                    Log.i("UsersManager","Found user with email: " + email);
                    future.complete(res.user);
                })
                .exceptionally(e -> {
                    Log.i("UsersManager","Could not find user with email: " + email + "\n" + e.toString());
                    future.completeExceptionally(e);
                    return null;
                });
        return future;
    }

}
