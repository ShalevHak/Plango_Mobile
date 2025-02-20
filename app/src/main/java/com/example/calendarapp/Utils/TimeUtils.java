package com.example.calendarapp.Utils;

import com.example.calendarapp.API.Interfaces.Event;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static boolean isEventOnDate(Event event, Date date) {
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
