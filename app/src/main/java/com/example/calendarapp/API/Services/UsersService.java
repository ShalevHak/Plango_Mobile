package com.example.calendarapp.API.Services;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.FetchingHelpers.UsersHelper;
import com.example.calendarapp.API.RequestsBody.LoginBody;
import com.example.calendarapp.API.RequestsBody.SignUpBody;
import com.example.calendarapp.API.Responses.AuthResponse;
import com.example.calendarapp.API.Responses.GetGroupsResponse;
import com.example.calendarapp.API.Responses.UserResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersService extends AbstractAPIService<UsersHelper> {
    private String USER_URL;

    public UsersService() {
        super(UsersHelper.class);
    }
    @Override
    protected void initRetrofit() {
        USER_URL = API.getRoute("users");
        this.retrofit = new Retrofit.Builder()
                .baseUrl(USER_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public CompletableFuture<AuthResponse> login(String email, String password) {
        CompletableFuture<AuthResponse> future = new CompletableFuture<>();
        Call<AuthResponse> call = fetchingHelper.login(new LoginBody(email, password));

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse data = response.body();
                    String token = data.token;
                    String userID = data.user.get("id").toString();
                    tokenManager.saveToken(token);
                    tokenManager.saveUserID(userID);
                    future.complete(data);
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public CompletableFuture<AuthResponse> signup(String name, String email, String password, String passwordConfirm) {
        CompletableFuture<AuthResponse> future = new CompletableFuture<>();
        Call<AuthResponse> call = fetchingHelper.signup(new SignUpBody(name, email, password, passwordConfirm));

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse data = response.body();
                    String token = data.token;
                    String userID = data.user.get("id").toString();
                    tokenManager.saveToken(token);
                    tokenManager.saveUserID(userID);
                    future.complete(data);
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public CompletableFuture<Void> logout() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Call<Void> call = fetchingHelper.logout();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    tokenManager.clearToken();
                    future.complete(null);
                } else {
                    future.completeExceptionally(new Exception("Logout failed with status: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public CompletableFuture<UserResponse> getUserById(String userId) {
        CompletableFuture<UserResponse> future = new CompletableFuture<>();

        if (userId == null || userId.isEmpty()) {
            future.completeExceptionally(new IllegalArgumentException("User ID is required"));
            return future;
        }

        Call<UserResponse> call = fetchingHelper.getUserById(userId);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("UsersService", "Request getUserById was successful");
                    future.complete(response.body());
                } else {
                    Log.e("UsersService", "Request getUserById returned empty response");
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("UsersService", "Request getUserById failed with id " + userId + ": \n"+ t.getMessage());
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public CompletableFuture<GetGroupsResponse> getGroupsById(String userId) {
        CompletableFuture<GetGroupsResponse> future = new CompletableFuture<>();

        if (userId == null || userId.isEmpty()) {
            future.completeExceptionally(new IllegalArgumentException("User ID is required"));
            return future;
        }

        Call<GetGroupsResponse> call = fetchingHelper.getUserGroupsByID(userId);

        call.enqueue(new Callback<GetGroupsResponse>() {
            @Override
            public void onResponse(Call<GetGroupsResponse> call, Response<GetGroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("UsersService", "Request getGroupsById was successful");
                    future.complete(response.body());
                } else {
                    Log.e("UsersService", "Request getGroupsById returned empty response");
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<GetGroupsResponse> call, Throwable t) {
                Log.e("UsersService", "Request getGroupsById failed with id " + userId + ": \n"+ t.getMessage());
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    public CompletableFuture<UserResponse> searchUserByEmail(String email) {
        CompletableFuture<UserResponse> future = new CompletableFuture<>();

        if (email == null || email.isEmpty()) {
            future.completeExceptionally(new IllegalArgumentException("User email is required"));
            return future;
        }

        Call<UserResponse> call = fetchingHelper.searchUsersByEmail(email);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("UsersService", "Request getGroupsById was successful");
                    future.complete(response.body());
                } else {
                    Log.e("UsersService", "Request getGroupsById returned empty response");
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("UsersService", "Request getGroupsById failed with id " + email + ": \n"+ t.getMessage());
                future.completeExceptionally(t);
            }
        });

        return future;
    }



    public CompletableFuture<UserResponse> getMe() {
        String myId = tokenManager.getUserID();
        return getUserById(myId);
    }



}
