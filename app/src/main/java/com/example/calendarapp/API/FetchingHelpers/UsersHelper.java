package com.example.calendarapp.API.FetchingHelpers;

import com.example.calendarapp.API.RequestsBody.LoginBody;
import com.example.calendarapp.API.RequestsBody.LogoutBody;
import com.example.calendarapp.API.RequestsBody.SignUpBody;
import com.example.calendarapp.API.Responses.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsersHelper {
    @POST("login")
    Call<AuthResponse> login(@Body LoginBody loginBody);
    @POST("signup")
    Call<AuthResponse> signup(@Body SignUpBody signupBody);
    @POST("logout")
    Call<AuthResponse> logout(@Body LogoutBody logoutBody);

}
