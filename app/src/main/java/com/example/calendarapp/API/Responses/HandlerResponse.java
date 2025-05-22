package com.example.calendarapp.API.Responses;

import com.example.calendarapp.API.Interfaces.Event;
import com.google.gson.annotations.SerializedName;

public class HandlerResponse<T> {
    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public T data;

}
