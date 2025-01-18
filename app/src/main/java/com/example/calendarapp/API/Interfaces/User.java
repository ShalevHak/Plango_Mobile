package com.example.calendarapp.API.Interfaces;

import androidx.annotation.NonNull;

public class User {
    static int userCounter = 0, id;
    public String email, password,name;

    public User() {
        id = userCounter++;
    }

    @NonNull
    @Override
    public String toString() {
        return "Email: " + email+"\n Name: "+ name + "\n Password: " + password;
    }
}
