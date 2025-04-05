package com.example.calendarapp.API.Services;

import com.example.calendarapp.API.FetchingHelpers.UsersHelper;
import com.example.calendarapp.API.Interceptors.AuthInterceptor;
import com.example.calendarapp.API.TokenManagement.TokenManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AbstractAPIService<Helper> {
    protected Retrofit retrofit;
    protected Helper fetchingHelper;
    protected static final TokenManager tokenManager = TokenManager.getInstance();
    protected static final  OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor(tokenManager))
            .build();;

    protected AbstractAPIService(Class<Helper> helperClass) {
        initRetrofit();
        this.fetchingHelper = retrofit.create(helperClass);

    }
    protected OkHttpClient getClient(){
        return client;
    }
    protected abstract void initRetrofit();
    protected String parseError(Response<?> response) {
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

