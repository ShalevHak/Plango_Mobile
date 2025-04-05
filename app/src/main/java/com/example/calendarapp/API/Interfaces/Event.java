package com.example.calendarapp.API.Interfaces;

import com.google.gson.annotations.SerializedName;
import com.example.calendarapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Event {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String title;

    @SerializedName("about")
    private String description;

    @SerializedName("start")
    private Date startDate;

    @SerializedName("end")
    private Date endDate;

    @SerializedName("fullDay")
    private Boolean fullDay;
    @SerializedName("color")
    private String color;

    public static final String DEFAULT_COLOR = "#3070a3";

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

    // Constructors
    public Event(String title, Date startDate, Date endDate) {
        validateDates(startDate, endDate);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = calcIsFullDay(startDate, endDate);
        this.color = DEFAULT_COLOR;
    }

    public Event(String title, String description, Date startDate, Date endDate) {
        validateDates(startDate, endDate);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = calcIsFullDay(startDate, endDate);
        this.color = DEFAULT_COLOR;
    }

    public Event(String title, String description, Date startDate, Date endDate, String color) {
        validateDates(startDate, endDate);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = calcIsFullDay(startDate, endDate);
        this.color = color;
    }

    public Event(String title, String description, Date startDate, Date endDate, boolean fullDay) {
        validateDates(startDate, endDate);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = fullDay;
        this.color = DEFAULT_COLOR;
    }

    // Validation and logic
    private void validateDates(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    private Boolean calcIsFullDay(Date startDate, Date endDate) {
        return getDurationHours() >= 24;
    }

    // Utility methods
    public float getDurationHours() {
        long durationMillis = endDate.getTime() - startDate.getTime();
        return durationMillis / (1000f * 60 * 60);
    }

    public float getDurationMS() {
        return endDate.getTime() - startDate.getTime();
    }

    public int getStartHour() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public String getFormattedStartDate() {
        return dateFormat.format(startDate);
    }

    public String getFormattedEndDate() {
        return dateFormat.format(endDate);
    }

    public boolean overlaps(Event other) {
        return !(this.endDate.before(other.startDate) || this.startDate.after(other.endDate));
    }

    public boolean startBefore(Event other) {
        return this.startDate.before(other.startDate);
    }

    public boolean endBefore(Event other) {
        return this.endDate.before(other.endDate);
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Boolean isFullDay() {
        return fullDay;
    }

    public String getColor() {
        return color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return title + " (" + getFormattedStartDate() + " - " + getFormattedEndDate() + ")";
    }
    public String getId(){
        return id;
    }
}
