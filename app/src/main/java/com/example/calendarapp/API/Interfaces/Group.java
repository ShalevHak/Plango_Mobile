package com.example.calendarapp.API.Interfaces;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class Group {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;
    @SerializedName("color")
    private String color;

    @SerializedName("about")
    private String about;
    @SerializedName("visibility")
    private String visibility;

    // Map Emails to Roles
    @SerializedName("members")
    private HashMap<String,String> members;
    @SerializedName("calendars")
    private List<ApiCalendar> calendars; //Virtual in Mongo

    // TODO: move to correct interfaces: IIdentifiers, IProfileCard, IMembers, IVisibility
    public static final String[] visibilityOptions = {"Private", "Public", "Protected"};
    public static final String[] Roles = {"owner","admin","member"};

    public static final String MEMBER = Roles[2];
    public static final String ADMIN = Roles[1];
    public static final String OWNER = Roles[0];
    // Constructors
    public Group(String name) {
        this.name = name;
    }

    public Group(String name, String about, String color, String visibility) {
        this.name = name;
        this.about = about;
        this.color = color;
        this.visibility = visibility;
    }

    public Group(String name, String about, String color, String visibility, HashMap<String,String> members)  {
        this.name = name;
        this.about = about;
        this.color = color;
        this.visibility = visibility;
        this.members = members;
    }

    public Group(Group group) {
        this.name = group.getName();
        this.about = group.getAbout();
        this.color = group.getColor();
        this.visibility = group.getVisibility();
        this.members = group.getMembers();
    }


    public String getColor() {
        return color;
    }

    public String getAbout() {
        return about;
    }

    public String getVisibility() {
        return visibility;
    }

    public HashMap<String, String> getMembers() {
        return members;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<ApiCalendar> getCalendars() {
        return calendars;
    }

    public void removeMember(String email){
        if(members.containsKey(email)) members.remove(email);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", about='" + about + '\'' +
                ", visibility='" + visibility + '\'' +
                ", members=" + members +
                '}';
    }
}
