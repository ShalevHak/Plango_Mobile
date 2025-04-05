package com.example.calendarapp.API.Services;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.FetchingHelpers.CalendarHelper;
import com.example.calendarapp.API.FetchingHelpers.UsersHelper;
import com.example.calendarapp.API.TokenManagement.TokenManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarsService extends AbstractAPIService<CalendarHelper> {
    private String Calendars_URL;
    public CalendarsService(){
        super(CalendarHelper.class);
    }
    @Override
    protected void initRetrofit() {
        Calendars_URL = API.getRoute("calendars");
        this.retrofit = new Retrofit.Builder()
                .baseUrl(Calendars_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
