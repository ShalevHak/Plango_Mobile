package com.example.calendarapp.API.Responses;

public class AuthResponse {
    public String status;
    public String message;
    public String data;
    public String error;
    public String stack;


    @Override
    public String toString() {
        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
