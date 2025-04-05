package com.example.calendarapp.API.FetchingHelpers;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.API.Responses.EventHandlerResponse;
import com.example.calendarapp.API.Responses.EventsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ActivitiesHelper {
    @GET("{calendarId}/events/by-date")
    Call<EventsResponse> getEventsByDate(
        @Path("calendarId") String calendarId,
        @Query("date") String date
    );

    @POST("{calendarId}/events/")
    Call<EventHandlerResponse> createEvent(
        @Path("calendarId") String calendarId,
        @Body Event event
    );
    @PATCH("{calendarId}/events/{eventId}")
    Call<EventHandlerResponse> updateEvent(
            @Path("calendarId") String calendarId,
            @Path("eventId") String eventId,
            @Body Event event
    );
}
