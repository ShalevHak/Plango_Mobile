package com.example.calendarapp.API.Interfaces;

import com.example.calendarapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

public class Event {
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Boolean fullDay;
    private int color;
    public static int DEFAULT_COLOR = R.attr.eventColor1; // Purple
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());


    public Event(String title, Date startDate, Date endDate) {
        validateDates(startDate, endDate);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = calcIsFullDay(startDate,endDate);
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

    public Event(String title, String description, Date startDate, Date endDate, int color) {
        validateDates(startDate, endDate);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = calcIsFullDay(startDate, endDate);
        this.color = color;
    }

    public Event(String title, String description, Date startDate, Date endDate, boolean fullDay, int color) {
        validateDates(startDate, endDate);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = fullDay;
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
    public Event(String title, Date startDate, Date endDate, boolean fullDay) {
        validateDates(startDate, endDate);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = fullDay;
        this.color = DEFAULT_COLOR;
    }
    private void validateDates(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }
    private Boolean calcIsFullDay(Date startDate, Date endDate) {
        // Calculate the duration in milliseconds
        float duration = this.getDurationHours();
        return duration >= 24;
    }
    // Returns event duration in hours
    public float getDurationHours() {
        long durationMillis = endDate.getTime() - startDate.getTime();
        long millisInHours = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        return durationMillis / (1000f * 60 * 60);
    }
    public float getDurationMS() {
        return endDate.getTime() - startDate.getTime();
    }
    // Returns event start hour (0-23)
    public int getStartHour() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    // Returns formatted start date
    public String getFormattedStartDate() {
        return dateFormat.format(startDate);
    }

    // Returns formatted end date
    public String getFormattedEndDate() {
        return dateFormat.format(endDate);
    }
    public String getTitle() {
        return title;
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
    public int getColor() {
        return color;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return title + " (" + getFormattedStartDate() + " - " + getFormattedEndDate() + ")";
    }
    // Checks if two events overlap
    public boolean overlaps(Event other) {
        return !(this.endDate.before(other.startDate) || this.startDate.after(other.endDate));
    }

    public boolean startBefore(Event other){
        return this.startDate.before(other.startDate);
    }
    public boolean endBefore(Event other){
        return this.endDate.before(other.endDate);
    }


    public String getDescription() {
        return description;
    }
}
