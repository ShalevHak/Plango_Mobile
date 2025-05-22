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

public class Group implements Parcelable {
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

    private Bitmap profilePicture; // Stores the Bitmap directly

    // TODO: move to correct interfaces: IIdentifiers, IProfileCard, IMembers, IVisibility
    public static final String[] visibilityOptions = {"Private", "Public", "Protected"};
    public static final String[] Roles = {"owner","admin","member"};


    // Constructors
    public Group(String name) {
        this.name = name;
        this.profilePicture = generateDefaultProfilePicture(name);
    }

    public Group(String name, String about, String color, String visibility) {
        this.name = name;
        this.about = about;
        this.color = color;
        this.visibility = visibility;
        this.profilePicture = generateDefaultProfilePicture(name);
    }

    public Group(String name, String about, String color, String visibility, HashMap<String,String> members)  {
        this.name = name;
        this.about = about;
        this.color = color;
        this.visibility = visibility;
        this.profilePicture = generateDefaultProfilePicture(name);
        this.members = members;
    }

    public Group(Group group) {
        this.name = group.getName();
        this.about = group.getAbout();
        this.color = group.getColor();
        this.visibility = group.getVisibility();
        this.profilePicture =  group.getProfilePicture();
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

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    // Method to generate the default profile picture
    private Bitmap generateDefaultProfilePicture(String name) {
        int width = 100;  // Width of the image
        int height = 100; // Height of the image

        // Create a Bitmap and Canvas to draw on
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw the background color (grey)
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        // Set up the paint for the text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Get the first two letters of the name
        String initials = name.length() >= 2 ? name.substring(0, 2).toUpperCase() : name.toUpperCase();

        // Calculate text position
        float x = width / 2f;
        float y = (height / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);

        // Draw the text
        canvas.drawText(initials, x, y, textPaint);

        return bitmap;
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
                ", profilePicture=" + profilePicture +
                '}';
    }


    // ─── Parcelable boilerplate ───
    protected Group(Parcel in) {
        id         = in.readString();
        name       = in.readString();
        about      = in.readString();
        visibility = in.readString();
        members    = (HashMap<String, String>) in.readSerializable();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(about);
        dest.writeString(visibility);
        dest.writeSerializable(members);
    }

    @Override public int describeContents() { return 0; }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override public Group createFromParcel(Parcel in) { return new Group(in); }
        @Override public Group[] newArray(int size) { return new Group[size]; }
    };
}
