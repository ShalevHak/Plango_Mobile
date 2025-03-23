package com.example.calendarapp.API.Services;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.FetchingHelpers.UsersHelper;
import com.example.calendarapp.API.Interceptors.AuthInterceptor;
import com.example.calendarapp.API.Interfaces.User;
import com.example.calendarapp.API.RequestsBody.LoginBody;
import com.example.calendarapp.API.RequestsBody.SignUpBody;
import com.example.calendarapp.API.Responses.AuthResponse;
import com.example.calendarapp.API.TokenManagement.TokenManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersService {
    private final String USER_URL = API.getRoute("users");
    private final Retrofit retrofit ;
    private final UsersHelper fetchingHelper;


    public UsersService() {
        TokenManager tokenManager = TokenManager.getInstance();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager)) // Attach JWT to requests
                .build();

        this.retrofit  = new Retrofit.Builder()
                .baseUrl(USER_URL)
                .client(client) // Uses OkHttpClient for network requests
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.fetchingHelper = retrofit.create(UsersHelper.class);
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    public final AuthCallback defaultAuthCallback = new AuthCallback() {
            @Override
            public void onSuccess() {
                Log.i("AuthCallback","Success");
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("AuthCallback","Error: " + errorMessage);
            }
        };
    public void login(String email, String password, AuthCallback authCallback){
        final AuthCallback callback = authCallback != null ? authCallback : defaultAuthCallback;
        Call<AuthResponse> call = fetchingHelper.login(new LoginBody(email,password));
        Log.i("LOGIN","Login");

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse data = response.body();
                    Log.i("LOGIN","Response: " + data.toString());

                    String token = data.token;
                    String userID = data.user.get("id").toString();
                    TokenManager.getInstance().saveToken(token); // Store token globally
                    TokenManager.getInstance().saveUserID(userID); // Store userID globally

                    callback.onSuccess();
                } else {
                    callback.onError(parseError(response));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError("Network Failure: " + t.getMessage());
            }
        });
    }
    public void signup(String name, String email, String password, String passwordConfirm, AuthCallback authCallback) {
        final AuthCallback callback = authCallback != null ? authCallback : defaultAuthCallback;
        Call<AuthResponse> call = fetchingHelper.signup(new SignUpBody(name,email,password,passwordConfirm));
        Log.i("SIGNUP","Sign up");

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse data = response.body();
                    Log.i("SIGNUP","Response: " + data.toString());

                    String token = data.token;
                    String userID = data.user.get("id").toString();
                    TokenManager.getInstance().saveToken(token); // Store token globally
                    TokenManager.getInstance().saveUserID(userID); // Store userID globally

                    TokenManager.getInstance().saveToken(token); // Store token globally
                    callback.onSuccess();
                } else {
                    callback.onError(parseError(response));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError("Network Failure: " + t.getMessage());
            }
        });
    }
    public void logout(AuthCallback authCallback) {
        final AuthCallback callback = authCallback != null ? authCallback : defaultAuthCallback;
        Call<Void> call = fetchingHelper.logout();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    TokenManager.getInstance().clearToken(); // Remove token globally
                    callback.onSuccess();
                } else {
                    callback.onError("Logout failed with status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network Failure: " + t.getMessage());
            }
        });
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
