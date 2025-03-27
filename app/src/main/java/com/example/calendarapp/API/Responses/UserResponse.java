package com.example.calendarapp.API.Responses;

import com.example.calendarapp.API.Interfaces.User;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public User user;
}
