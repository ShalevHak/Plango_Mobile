package com.example.calendarapp.Managers;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CalendarsManager {
    private static CalendarsManager calendarsManager;
    private Calendar currentCalendar;
    private CalendarsManager(){
        this.currentCalendar = Calendar.getInstance();
    }

    public static CalendarsManager getInstance(){
        if (calendarsManager == null) {
            Log.i("CalendarService","Init CalendarService");
            synchronized (CalendarsManager.class) { // Thread-safe
                if (calendarsManager == null) {
                    calendarsManager = new CalendarsManager();
                }
            }
        }
        return calendarsManager;
    }

    public void add(int field, int amount) {
        currentCalendar.add(field,amount);
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    public Date GetCurrentDay() {
        return currentCalendar.getTime();
    }

    public CompletableFuture<String> getMyScheduleId() {
        CompletableFuture<String> future = new CompletableFuture<>();
        API.api().usersService.getMe().thenAccept(userResponse -> {
            future.complete(userResponse.user.getScheduleId());
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }
    public CompletableFuture<List<Event>> GetCurrentDayEvent() {
        Date today = GetCurrentDay();
        return getMyScheduleId()
                .thenCompose(scheduleId -> API.api().activitiesService.getEventsByDate(scheduleId, today));
    }

    public CompletableFuture<Event> addEvent(Event event, String calendarId) {
        return API.api().activitiesService.addEvent(calendarId,event);
    }

    public CompletableFuture<Event> updateEvent(Event originalEvent, Event editedEvent, String calendarId) {
        return API.api().activitiesService.updateEvent(calendarId,originalEvent.getId(),editedEvent);
    }

}
