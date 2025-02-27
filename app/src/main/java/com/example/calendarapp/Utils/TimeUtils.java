package com.example.calendarapp.Utils;

import com.example.calendarapp.API.Interfaces.Event;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static boolean isEventOnDate(Event event, Date date) {
        // Normalize event start date to midnight
        Calendar eventStart = Calendar.getInstance();
        eventStart.setTime(event.getStartDate());
        eventStart.set(Calendar.HOUR_OF_DAY, 0);
        eventStart.set(Calendar.MINUTE, 0);
        eventStart.set(Calendar.SECOND, 0);
        eventStart.set(Calendar.MILLISECOND, 0);

        // Normalize event end date to midnight
        Calendar eventEnd = Calendar.getInstance();
        eventEnd.setTime(event.getEndDate());
        eventEnd.set(Calendar.HOUR_OF_DAY, 0);
        eventEnd.set(Calendar.MINUTE, 0);
        eventEnd.set(Calendar.SECOND, 0);
        eventEnd.set(Calendar.MILLISECOND, 0);

        // Normalize the provided date to midnight
        Calendar targetDay = Calendar.getInstance();
        targetDay.setTime(date);
        targetDay.set(Calendar.HOUR_OF_DAY, 0);
        targetDay.set(Calendar.MINUTE, 0);
        targetDay.set(Calendar.SECOND, 0);
        targetDay.set(Calendar.MILLISECOND, 0);

        // Check if the target day is between the start and end dates (inclusive)
        return !targetDay.before(eventStart) && !targetDay.after(eventEnd);
    }

    public static int getMinuteOfDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
    };
}
