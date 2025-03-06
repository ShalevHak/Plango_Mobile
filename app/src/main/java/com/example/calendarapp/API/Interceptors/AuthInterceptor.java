package com.example.calendarapp.API.Interceptors;
import com.example.calendarapp.API.TokenManagement.TokenManager;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = tokenManager.getToken();
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();

        if (token != null) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}
