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
        // TODO: remove when not needed
        createSampleEvents();
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
    private List<Event> events;
    private  void createSampleEvents() {
        events = new ArrayList<>();

        events.add(new Event("New Year Planning", parseDate("2024-12-30 09:00"), parseDate("2024-12-30 11:00")));
        events.add(new Event("Year-End Review", parseDate("2024-12-31 10:00"), parseDate("2024-12-31 12:00")));
        events.add(new Event("New Year's Eve Party", parseDate("2024-12-31 20:00"), parseDate("2025-01-01 02:00")));

        events.add(new Event("Kickoff Meeting", parseDate("2025-01-02 09:00"), parseDate("2025-01-02 10:30")));
        events.add(new Event("Project Alpha Launch", parseDate("2025-01-03 13:00"), parseDate("2025-01-03 15:00")));
        events.add(new Event("Weekend Retreat", parseDate("2025-01-04 08:00"), parseDate("2025-01-05 18:00")));

        events.add(new Event("Team Sync", parseDate("2025-01-06 11:00"), parseDate("2025-01-06 12:00")));
        events.add(new Event("Client Presentation", parseDate("2025-01-07 14:00"), parseDate("2025-01-07 16:00")));
        events.add(new Event("Product Demo", parseDate("2025-01-09 10:00"), parseDate("2025-01-09 11:00")));

        events.add(new Event("Weekly Standup", parseDate("2025-01-10 09:00"), parseDate("2025-01-10 09:30")));
        events.add(new Event("Workshop: Leadership Skills", parseDate("2025-01-12 13:00"), parseDate("2025-01-12 17:00")));
        events.add(new Event("Sprint Planning", parseDate("2025-01-13 09:00"), parseDate("2025-01-13 10:30")));

        events.add(new Event("Mid-Month Review", parseDate("2025-01-15 15:00"), parseDate("2025-01-15 17:00")));
        events.add(new Event("Company Anniversary", parseDate("2025-01-16 18:00"), parseDate("2025-01-16 22:00")));
        events.add(new Event("Out of Office", parseDate("2025-01-17 00:00"), parseDate("2025-01-19 23:59")));

        events.add(new Event("Budget Review", parseDate("2025-01-20 10:00"), parseDate("2025-01-20 12:00")));
        events.add(new Event("Budget Review", parseDate("2025-01-20 10:00"), parseDate("2025-01-20 12:00")));
        events.add(new Event("Team Building Exercise", parseDate("2025-01-21 14:00"), parseDate("2025-01-21 17:00")));
        events.add(new Event("Annual Report Prep", parseDate("2025-01-22 09:00"), parseDate("2025-01-23 18:00")));

        events.add(new Event("Product Roadmap Review", parseDate("2025-01-24 11:00"), parseDate("2025-01-24 13:00")));
        events.add(new Event("Weekend Hackathon", parseDate("2025-01-25 08:00"), parseDate("2025-01-26 20:00")));

        events.add(new Event("Strategy Meeting", parseDate("2025-01-27 09:00"), parseDate("2025-01-27 10:30")));
        events.add(new Event("Industry Webinar", parseDate("2025-01-28 16:00"), parseDate("2025-01-28 18:00")));

        events.add(new Event("Quarterly Review", parseDate("2025-01-29 10:00"), parseDate("2025-01-29 12:00")));
        events.add(new Event("Team Lunch", parseDate("2025-01-30 12:30"), parseDate("2025-01-30 14:00")));
        events.add(new Event("Leadership Offsite", parseDate("2025-01-31 07:00"), parseDate("2025-02-02 19:00")));

        events.add(new Event("Conference Kickoff", parseDate("2025-02-20 09:00"), parseDate("2025-02-20 12:00")));
        events.add(new Event("Team Strategy Session", parseDate("2025-02-20 10:30"), parseDate("2025-02-20 12:30")));
        events.add(new Event("Networking Lunch", parseDate("2025-02-20 12:00"), parseDate("2025-02-20 13:30")));
        events.add(new Event("Panel Discussion", parseDate("2025-02-20 13:00"), parseDate("2025-02-20 15:00")));

        events.add(new Event("Product Demo", parseDate("2025-02-21 09:00"), parseDate("2025-02-21 10:30")));
        events.add(new Event("Customer Feedback Session", parseDate("2025-02-21 10:00"), parseDate("2025-02-21 11:30")));
        events.add(new Event("Innovation Workshop", parseDate("2025-02-21 11:00"), parseDate("2025-02-21 12:30")));
        events.add(new Event("Investor Meeting", parseDate("2025-02-21 12:00"), parseDate("2025-02-21 13:00")));

        events.add(new Event("Keynote Speech", parseDate("2025-02-22 08:30"), parseDate("2025-02-22 11:00")));
        events.add(new Event("Breakout Session 1", parseDate("2025-02-22 9:00"), parseDate("2025-02-22 11:30")));
        events.add(new Event("Breakout Session 2", parseDate("2025-02-22 10:15"), parseDate("2025-02-22 11:45")));
        events.add(new Event("Breakout Session 3", parseDate("2025-02-22 10:30"), parseDate("2025-02-22 12:00")));
        events.add(new Event("Networking Event", parseDate("2025-02-22 15:00"), parseDate("2025-02-22 17:00")));

        events.add(new Event("Roundtable Discussion", parseDate("2025-02-23 09:00"), parseDate("2025-02-23 11:00")));
        events.add(new Event("Expert Q&A Panel", parseDate("2025-02-23 11:00"), parseDate("2025-02-23 12:30")));
        events.add(new Event("Sponsor Showcase", parseDate("2025-02-23 13:00"), parseDate("2025-02-23 14:30")));
        events.add(new Event("Closing Remarks", parseDate("2025-02-23 17:00"), parseDate("2025-02-23 18:00")));

        events.add(new Event("VIP Dinner", parseDate("2025-02-24 19:00"), parseDate("2025-02-24 22:00")));
        events.add(new Event("Hackathon", parseDate("2025-02-24 08:00"), parseDate("2025-02-25 20:00")));
        events.add(new Event("All-Day Coding Workshop", parseDate("2025-02-25 09:00"), parseDate("2025-02-25 17:00")));
        events.add(new Event("Tech Demo", parseDate("2025-02-25 10:00"), parseDate("2025-02-25 12:00")));
        events.add(new Event("Wrap-Up Discussion", parseDate("2025-02-25 16:00"), parseDate("2025-02-25 17:30")));
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
    public List<Event> getSampleEvents(){
        return events;
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
                    Event fetchedEvent = data.data.doc;
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
                    Event fetchedEvent = data.data.doc;
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
    public void addEvent(Event event) {
        // TODO: update database;
        Log.i("ActivitiesService","Adding event");
        events.add(event);
    }

    public void updateEvent(Event originalEvent, Event editedEvent) {
        // TODO: update database;

        Log.i("ActivitiesService","Updating event");
        events.replaceAll(event -> event.equals(originalEvent) ? editedEvent : event);    }


}
