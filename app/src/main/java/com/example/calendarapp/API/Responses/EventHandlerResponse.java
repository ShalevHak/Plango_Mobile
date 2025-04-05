package com.example.calendarapp.API.Responses;

import com.example.calendarapp.API.Interfaces.Event;
import com.google.gson.annotations.SerializedName;

public class EventHandlerResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public DataWrapper data;

    public static class DataWrapper {
        @SerializedName("doc")
        public Event doc;
    }
}
