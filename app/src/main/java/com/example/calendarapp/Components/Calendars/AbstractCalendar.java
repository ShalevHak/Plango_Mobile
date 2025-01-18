package com.example.calendarapp.Components.Calendars;

import com.example.calendarapp.API.Interfaces.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractCalendar {
    protected Calendar currentCalendar;

    public AbstractCalendar() {
        this.currentCalendar = Calendar.getInstance();
    }

    public abstract List<Event> getEventsForDate(Date date);

    public abstract void navigate(int offset);

    public Date getCurrentDate() {
        return currentCalendar.getTime();
    }
}
