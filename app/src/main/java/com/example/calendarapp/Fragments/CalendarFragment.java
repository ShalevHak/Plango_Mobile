
package com.example.calendarapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Components.Calendars.CalendarContainer;
import com.example.calendarapp.Controllers.CalendarCoordinator;
import com.example.calendarapp.R;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    private CalendarContainer calendarContainer;
    private CalendarCoordinator calendarCoordinator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

//        // Initialize the CalendarContainer
//        calendarContainer = view.findViewById(R.id.calendarContainer);
//
//        // Prepare sample events for DayCalendar
//        List<Event> sampleEvents = getSampleEvents();
//
//        // Initialize DayCalendar with the sample events
//        DayCalendar dayCalendar = new DayCalendar(sampleEvents);
//
//        // Set up the CalendarCoordinator
//        calendarCoordinator = new CalendarCoordinator(calendarContainer, dayCalendar);

        return view;
    }

    private List<Event> getSampleEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Meeting with Team", parseDate("2024-12-14 07:00"), parseDate("2024-12-14 08:00")));
        events.add(new Event("Lunch with Alex", parseDate("2024-12-13 12:00"), parseDate("2024-12-13 13:00")));
        events.add(new Event("Project Presentation", parseDate("2024-12-14 8:00"), parseDate("2024-12-14 10:00")));
        events.add(new Event("Alex - Out of work", parseDate("2024-12-14 00:00"), parseDate("2024-12-16 00:00")));

        events.add(new Event("Code Review", parseDate("2024-12-13 09:00"), parseDate("2024-12-13 11:00")));
        events.add(new Event("Team Sync", parseDate("2024-12-14 10:00"), parseDate("2024-12-14 11:30")));
        events.add(new Event("Client Meeting", parseDate("2024-12-14 15:00"), parseDate("2024-12-14 17:45")));
        events.add(new Event("Break", parseDate("2024-12-14 18:00"), parseDate("2024-12-14 19:00")));
        events.add(new Event("Dinner With Client", parseDate("2024-12-14 19:00"), parseDate("2024-12-15 00:00")));
        events.add(new Event("Out of work", parseDate("2024-12-15 00:00"), parseDate("2024-12-17 00:00")));
        events.add(new Event("Meeting with Team", parseDate("2024-12-14 07:00"), parseDate("2024-12-14 08:00")));
        events.add(new Event("Lunch with Alex", parseDate("2024-12-13 12:00"), parseDate("2024-12-13 13:00")));
        events.add(new Event("Project Presentation", parseDate("2024-12-14 8:00"), parseDate("2024-12-14 10:00")));
        events.add(new Event("Alex - Out of work", parseDate("2024-12-14 00:00"), parseDate("2024-12-16 00:00")));

        events.add(new Event("Code Review", parseDate("2024-12-13 09:00"), parseDate("2024-12-13 11:00")));
        events.add(new Event("Team Sync", parseDate("2024-12-14 10:00"), parseDate("2024-12-14 11:30")));
        events.add(new Event("Client Meeting", parseDate("2024-12-14 15:00"), parseDate("2024-12-14 17:45")));
        events.add(new Event("Break", parseDate("2024-12-14 18:00"), parseDate("2024-12-14 19:00")));
        events.add(new Event("Dinner With Client", parseDate("2024-12-14 19:00"), parseDate("2024-12-15 00:00")));
        events.add(new Event("Out of work", parseDate("2024-12-15 00:00"), parseDate("2024-12-17 00:00")));
        events.add(new Event("Meeting with Team", parseDate("2024-12-14 07:00"), parseDate("2024-12-14 08:00")));
        events.add(new Event("Lunch with Alex", parseDate("2024-12-13 12:00"), parseDate("2024-12-13 13:00")));
        events.add(new Event("Project Presentation", parseDate("2024-12-14 8:00"), parseDate("2024-12-14 10:00")));
        events.add(new Event("Alex - Out of work", parseDate("2024-12-14 00:00"), parseDate("2024-12-16 00:00")));

        events.add(new Event("Code Review", parseDate("2024-12-13 09:00"), parseDate("2024-12-13 11:00")));
        events.add(new Event("Team Sync", parseDate("2024-12-14 10:00"), parseDate("2024-12-14 11:30")));
        events.add(new Event("Client Meeting", parseDate("2024-12-14 15:00"), parseDate("2024-12-14 17:45")));
        events.add(new Event("Break", parseDate("2024-12-14 18:00"), parseDate("2024-12-14 19:00")));
        events.add(new Event("Dinner With Client", parseDate("2024-12-14 19:00"), parseDate("2024-12-15 00:00")));
        events.add(new Event("Out of work", parseDate("2024-12-15 00:00"), parseDate("2024-12-17 00:00")));
        events.add(new Event("Meeting with Team", parseDate("2024-12-14 07:00"), parseDate("2024-12-14 08:00")));
        events.add(new Event("Lunch with Alex", parseDate("2024-12-13 12:00"), parseDate("2024-12-13 13:00")));
        events.add(new Event("Project Presentation", parseDate("2024-12-14 8:00"), parseDate("2024-12-14 10:00")));
        events.add(new Event("Alex - Out of work", parseDate("2024-12-14 00:00"), parseDate("2024-12-16 00:00")));

        events.add(new Event("Code Review", parseDate("2024-12-13 09:00"), parseDate("2024-12-13 11:00")));
        events.add(new Event("Team Sync", parseDate("2024-12-14 10:00"), parseDate("2024-12-14 11:30")));
        events.add(new Event("Client Meeting", parseDate("2024-12-14 15:00"), parseDate("2024-12-14 17:45")));
        events.add(new Event("Break", parseDate("2024-12-14 18:00"), parseDate("2024-12-14 19:00")));
        events.add(new Event("Dinner With Client", parseDate("2024-12-14 19:00"), parseDate("2024-12-15 00:00")));
        events.add(new Event("Out of work", parseDate("2024-12-15 00:00"), parseDate("2024-12-17 00:00")));
        return events;
    }
    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateTimeFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }
}

