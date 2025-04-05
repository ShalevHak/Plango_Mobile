package com.example.calendarapp.API.FetchingHelpers;

import com.example.calendarapp.API.Interfaces.User;
import com.example.calendarapp.API.RequestsBody.LoginBody;
import com.example.calendarapp.API.RequestsBody.SignUpBody;
import com.example.calendarapp.API.Responses.AuthResponse;
import com.example.calendarapp.API.Responses.GetGroupsResponse;
import com.example.calendarapp.API.Responses.UserResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsersHelper{
    @POST("login")
    Call<AuthResponse> login(@Body LoginBody loginBody);
    @POST("signup")
    Call<AuthResponse> signup(@Body SignUpBody signupBody);
    @POST("logout")
    Call<Void> logout();
    @GET("{userId}")
    Call<UserResponse> getUserById(@Path("userId") String userId);

    @GET("{userId}/groups")
    Call<GetGroupsResponse> getUserGroupsByID(@Path("userId") String userId);

    @GET("search")
    Call<UserResponse> searchUsersByEmail(@Query("email") String email);

}
