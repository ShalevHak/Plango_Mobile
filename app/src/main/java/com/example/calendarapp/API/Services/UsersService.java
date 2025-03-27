package com.example.calendarapp.API.Services;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.FetchingHelpers.UsersHelper;
import com.example.calendarapp.API.Interceptors.AuthInterceptor;
import com.example.calendarapp.API.RequestsBody.LoginBody;
import com.example.calendarapp.API.RequestsBody.SignUpBody;
import com.example.calendarapp.API.Responses.AuthResponse;
import com.example.calendarapp.API.Responses.UserResponse;
import com.example.calendarapp.API.TokenManagement.TokenManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersService {
    private final String USER_URL = API.getRoute("users");
    private final Retrofit retrofit;
    private final UsersHelper fetchingHelper;

    public UsersService() {
        TokenManager tokenManager = TokenManager.getInstance();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager))
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(USER_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.fetchingHelper = retrofit.create(UsersHelper.class);
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
                    TokenManager.getInstance().saveToken(token);
                    TokenManager.getInstance().saveUserID(userID);
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
                    TokenManager.getInstance().saveToken(token);
                    TokenManager.getInstance().saveUserID(userID);
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
                    TokenManager.getInstance().clearToken();
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

    public CompletableFuture<UserResponse> getMe() {
        String myId = TokenManager.getInstance().getUserID();
        return getUserById(myId);
    }

    private String parseError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                JsonObject jsonObject = JsonParser.parseString(errorBody).getAsJsonObject();
                if (jsonObject.has("message")) {
                    return jsonObject.get("message").getAsString();
                } else if (jsonObject.has("error")) {
                    return jsonObject.get("error").getAsString();
                } else {
                    return errorBody;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown error";
    }
}
