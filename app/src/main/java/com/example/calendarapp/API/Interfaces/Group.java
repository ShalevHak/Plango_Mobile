package com.example.calendarapp.API.Interfaces;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Member;
import java.util.HashMap;

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


    // TODO: move to correct interfaces: IIdentifiers, IProfileCard, IMembers, IVisibility
    public static final String[] visibilityOptions = {"Private", "Public", "Protected"};
    public static final String[] Roles = {"owner","admin","member"};


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

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public HashMap<String, String> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }


    public String getName() {
        return name;
    }




    public String getId() {
        return id;
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
