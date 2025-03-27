package com.example.calendarapp.API.FetchingHelpers;

import com.example.calendarapp.API.RequestsBody.LoginBody;
import com.example.calendarapp.API.RequestsBody.SignUpBody;
import com.example.calendarapp.API.Responses.AuthResponse;
import com.example.calendarapp.API.Responses.UserResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersHelper {
    @POST("login")
    Call<AuthResponse> login(@Body LoginBody loginBody);
    @POST("signup")
    Call<AuthResponse> signup(@Body SignUpBody signupBody);
    @POST("logout")
    Call<Void> logout();
    @GET("users/{id}")
    Call<UserResponse> getUserById(@Path("id") String id);

}
