package com.example.calendarapp.Components.Calendars;

import com.example.calendarapp.API.Interfaces.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DayCalendar extends AbstractCalendar {
    private List<Event> allEvents;

    public DayCalendar(List<Event> events) {
        this.allEvents = events;
    }

    @Override
    public List<Event> getEventsForDate(Date date) {
        return allEvents.stream()
                .filter(event -> isEventOnDate(event, date))
                .collect(Collectors.toList());
    }

    @Override
    public void navigate(int offset) {
        currentCalendar.add(Calendar.DAY_OF_MONTH, offset);
    }

    private boolean isEventOnDate(Event event, Date date) {
        Calendar eventStart = Calendar.getInstance();
        eventStart.setTime(event.getStartDate());
        Calendar eventEnd = Calendar.getInstance();
        eventEnd.setTime(event.getEndDate());

        Calendar today = Calendar.getInstance();
        today.setTime(date);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return (eventStart.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                eventStart.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR));
    }
}
