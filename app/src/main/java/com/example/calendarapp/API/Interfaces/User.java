package com.example.calendarapp.API.Interfaces;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class User {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("about")
    private String about;

    @SerializedName("email")
    private String email;

    @SerializedName("schedule")
    private String scheduleId;

    @SerializedName("systemRole")
    private String systemRole;

    @SerializedName("groups")
    private List<String> groupIds;

    @SerializedName("coverImgPath")
    private String coverImgPath;

    // Add constructor, getters, setters as needed
    public String getId(){
        return id;
    }

    public User(String name, String about, String email) {
        this.name = name;
        this.about = about;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}