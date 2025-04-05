package com.example.calendarapp.API.Responses;

import com.example.calendarapp.API.Interfaces.Event;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class EventsResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("results")
    public int results;

    @SerializedName("data")
    public List<Event> data;

    public EventsResponse() {

    }

    public EventsResponse(String status, List<Event> data) {
        this.status = status;
        this.data = data;
        this.results = data != null ? data.size() : 0;
    }
}