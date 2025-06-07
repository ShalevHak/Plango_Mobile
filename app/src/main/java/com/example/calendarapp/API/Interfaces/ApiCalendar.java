package com.example.calendarapp.API.Interfaces;

import com.google.gson.annotations.SerializedName;

public class ApiCalendar {
    @SerializedName("_id") private String id;
    @SerializedName("title") private String title;
    @SerializedName("about")
    private String about;
    @SerializedName("groupId") private String groupId;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAbout() {
        return about;
    }

    public String getGroupId() {
        return groupId;
    }
}
