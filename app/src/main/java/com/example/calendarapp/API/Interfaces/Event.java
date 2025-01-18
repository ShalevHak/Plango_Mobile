package com.example.calendarapp.API.Interfaces;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {
    private String title;
    private Date startDate;
    private Date endDate;
    private Boolean fullDay;

    public Event(String title, Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = calcIsFullDay(startDate,endDate);
    }

    private Boolean calcIsFullDay(Date startDate, Date endDate) {
        // Calculate the duration in milliseconds
        long durationInMillis = endDate.getTime() - startDate.getTime();

        // Check if the duration is greater than or equal to 24 hours
        long millisIn24Hours = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        return durationInMillis >= millisIn24Hours;
    }


    public Event(String title, Date startDate, Date endDate, Boolean fullDay) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fullDay = fullDay;
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
}
