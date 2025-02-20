package com.example.calendarapp.Services;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarService{
    private static CalendarService calendarService;
    private Calendar currentCalendar;
    private CalendarService(){
        this.currentCalendar = Calendar.getInstance();
    }
    public List<Event> GetCurrentDayEvent() {
        Date today = currentCalendar.getTime();

        // Filter events that occur today using isEventOnDate
        return API.api().activitiesService.getEvents()
                .stream()
                .filter(event -> TimeUtils.isEventOnDate(event, today))
                .collect(Collectors.toList());
    }
    public static CalendarService getInstance(){
        if (calendarService == null) {
            Log.i("CalendarService","Init CalendarService");
            synchronized (API.class) { // Thread-safe
                if (calendarService == null) {
                    calendarService = new CalendarService();
                }
            }
        }
        return calendarService;
    }

    public void add(int field, int amount) {
        currentCalendar.add(field,amount);
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }
}
