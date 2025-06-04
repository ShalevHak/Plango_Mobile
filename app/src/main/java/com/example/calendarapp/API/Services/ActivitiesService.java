package com.example.calendarapp.API.Services;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.FetchingHelpers.ActivitiesHelper;
import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.API.Responses.EventHandlerResponse;
import com.example.calendarapp.API.Responses.EventsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitiesService extends AbstractAPIService<ActivitiesHelper>{
    private String CALENDARS_URL;
    public ActivitiesService(){
        super(ActivitiesHelper.class);
    }
    @Override
    protected void initRetrofit() {
        CALENDARS_URL =  API.getRoute("calendars");
        Log.i("ActivitiesService","Base route: "+ CALENDARS_URL);
        this.retrofit = new Retrofit.Builder()
                .baseUrl(CALENDARS_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static final SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateTimeFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public CompletableFuture<List<Event>> getEventsByDate(String calendarID, Date date) {
        CompletableFuture<List<Event>> future = new CompletableFuture<>();

        String formattedDate = apiDateFormat.format(date); // Format date properly
        Call<EventsResponse> call = fetchingHelper.getEventsByDate(calendarID,formattedDate);

        call.enqueue(new Callback<EventsResponse>() {
            @Override
            public void onResponse(Call<EventsResponse> call, Response<EventsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EventsResponse data = response.body();
                    List<Event> fetchedEvents = data.data;
                    future.complete(fetchedEvents);
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<EventsResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }
    public CompletableFuture<Event> addEvent(String calendarID, Event event) {
        CompletableFuture<Event> future = new CompletableFuture<>();
        Call<EventHandlerResponse> call = fetchingHelper.createEvent(calendarID,event);

        call.enqueue(new Callback<EventHandlerResponse>() {
            @Override
            public void onResponse(Call<EventHandlerResponse> call, Response<EventHandlerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EventHandlerResponse data = response.body();
                    Event fetchedEvent = data.data;
                    future.complete(fetchedEvent);
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<EventHandlerResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }
    public CompletableFuture<Event> updateEvent(String calendarID, String eventID,Event event) {
        CompletableFuture<Event> future = new CompletableFuture<>();
        Call<EventHandlerResponse> call = fetchingHelper.updateEvent(calendarID,eventID,event);

        call.enqueue(new Callback<EventHandlerResponse>() {
            @Override
            public void onResponse(Call<EventHandlerResponse> call, Response<EventHandlerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EventHandlerResponse data = response.body();
                    Event fetchedEvent = data.data;
                    future.complete(fetchedEvent);
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<EventHandlerResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }
}
