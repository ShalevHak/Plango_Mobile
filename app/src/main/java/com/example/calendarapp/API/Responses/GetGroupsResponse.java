package com.example.calendarapp.API.Responses;

import com.example.calendarapp.API.Interfaces.Group;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGroupsResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public List<Group> groups;
}
