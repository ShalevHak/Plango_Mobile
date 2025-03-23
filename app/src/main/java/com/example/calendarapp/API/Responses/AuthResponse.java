package com.example.calendarapp.API.Responses;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("token") // Ensure this matches the backend response
    public String token;


    @SerializedName("error")
    public String error;

    @SerializedName("stack")
    public String stack;
    @SerializedName("user")
    public JsonObject user;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", error='" + error + '\'' +
                ", stack='" + stack + '\'' +
                ", userID'" + user.get("id").toString() + '\'' +
                '}';
    }
}
