package com.example.calendarapp.API.Interfaces;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class User {
    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("about")
    public String about;

    @SerializedName("email")
    public String email;

    @SerializedName("schedule")
    public String scheduleId;

    @SerializedName("systemRole")
    public String systemRole;

    @SerializedName("groups")
    public List<String> groupIds;

    @SerializedName("coverImgPath")
    public String coverImgPath;

    // Add constructor, getters, setters as needed

}