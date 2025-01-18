package com.example.calendarapp.API.Services;

import android.util.Log;

import com.example.calendarapp.API.FetchingHelpers.UsersHelper;
import com.example.calendarapp.API.RequestsBody.LoginBody;
import com.example.calendarapp.API.RequestsBody.SignUpBody;
import com.example.calendarapp.API.Responses.AuthResponse;
import com.example.calendarapp.API.Responses.LogoutResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersService {
      private final String USER_URL = "http://10.100.102.201:8000/api/v1-dev/users/";
//    private final String USER_URL = "http://192.168.43.105:8000/api/v1-dev/users/";
    private Retrofit retrofit ;
    private UsersHelper fetchingHelper;
    public static String token;


    public UsersService() {
        this.retrofit  = new Retrofit.Builder()
                .baseUrl(USER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.fetchingHelper = retrofit.create(UsersHelper.class);
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(String errorMessage);
    }
    public void Login(String email, String password, AuthCallback callback){
        Call<AuthResponse> call = fetchingHelper.login(new LoginBody(email,password));
        Log.i("LOGIN","Login");

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse data = response.body();
                    Log.i("LOGIN","Response: " + data.toString());
                    token = data.data;
                    callback.onSuccess();
                } else {
                    String errorMsg = "Error: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();

                            // Corrected JsonParser usage
                            JsonParser parser = new JsonParser();
                            JsonObject jsonObject = parser.parse(errorBody).getAsJsonObject();

                            // Extract the "message" or "error" field if available
                            if (jsonObject.has("message")) {
                                errorMsg = jsonObject.get("message").getAsString();
                            } else if (jsonObject.has("error")) {
                                errorMsg = jsonObject.get("error").getAsString();
                            } else {
                                errorMsg = errorBody;  // Fallback to raw error body
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorMsg = "Error parsing error body";
                    }
                    Log.e("LOGIN", errorMsg);
                    callback.onError(errorMsg);  // Notify error
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.message = "Network Failure: " + t.getMessage();
                Log.e("LOGIN", errorResponse.message);
                callback.onError(errorResponse.message);  // Notify network error
            }
        });
    }
    public void SignUp(String name, String email, String password, String passwordConfirm, AuthCallback callback) {
        Call<AuthResponse> call = fetchingHelper.signup(new SignUpBody(name,email,password,passwordConfirm));
        Log.i("SIGNUP","Sign up");

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse data = response.body();
                    Log.i("SIGNUP","Response: " + data.toString());
                    token = data.data;
                    callback.onSuccess();
                } else {
                    String errorMsg = "Error: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();

                            // Corrected JsonParser usage
                            JsonParser parser = new JsonParser();
                            JsonObject jsonObject = parser.parse(errorBody).getAsJsonObject();

                            // Extract the "message" or "error" field if available
                            if (jsonObject.has("message")) {
                                errorMsg = jsonObject.get("message").getAsString();
                            } else if (jsonObject.has("error")) {
                                errorMsg = jsonObject.get("error").getAsString();
                            } else {
                                errorMsg = errorBody;  // Fallback to raw error body
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorMsg = "Error parsing error body";
                    }
                    Log.e("SIGNUP", errorMsg);
                    callback.onError(errorMsg);  // Notify error
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.message = "Network Failure: " + t.getMessage();
                Log.e("SIGNIN", errorResponse.message);
                callback.onError(errorResponse.message);  // Notify network error
            }
        });
    }
    public void logout(){
//        Call<LogoutResponse> call = fetchingHelper.logout();
    }
}
