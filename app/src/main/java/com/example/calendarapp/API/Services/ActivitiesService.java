package com.example.calendarapp.API.Services;

import com.example.calendarapp.API.Interfaces.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitiesService {
    private List<Event> getSampleEvents() {
        List<Event> events = new ArrayList<>();

        events.add(new Event("New Year Planning", parseDate("2024-12-30 09:00"), parseDate("2024-12-30 11:00")));
        events.add(new Event("Year-End Review", parseDate("2024-12-31 10:00"), parseDate("2024-12-31 12:00")));
        events.add(new Event("New Year's Eve Party", parseDate("2024-12-31 20:00"), parseDate("2025-01-01 02:00")));

        events.add(new Event("Kickoff Meeting", parseDate("2025-01-02 09:00"), parseDate("2025-01-02 10:30")));
        events.add(new Event("Project Alpha Launch", parseDate("2025-01-03 13:00"), parseDate("2025-01-03 15:00")));
        events.add(new Event("Weekend Retreat", parseDate("2025-01-04 08:00"), parseDate("2025-01-05 18:00")));

        events.add(new Event("Team Sync", parseDate("2025-01-06 11:00"), parseDate("2025-01-06 12:00")));
        events.add(new Event("Client Presentation", parseDate("2025-01-07 14:00"), parseDate("2025-01-07 16:00")));
        events.add(new Event("Product Demo", parseDate("2025-01-09 10:00"), parseDate("2025-01-09 11:00")));

        events.add(new Event("Weekly Standup", parseDate("2025-01-10 09:00"), parseDate("2025-01-10 09:30")));
        events.add(new Event("Workshop: Leadership Skills", parseDate("2025-01-12 13:00"), parseDate("2025-01-12 17:00")));
        events.add(new Event("Sprint Planning", parseDate("2025-01-13 09:00"), parseDate("2025-01-13 10:30")));

        events.add(new Event("Mid-Month Review", parseDate("2025-01-15 15:00"), parseDate("2025-01-15 17:00")));
        events.add(new Event("Company Anniversary", parseDate("2025-01-16 18:00"), parseDate("2025-01-16 22:00")));
        events.add(new Event("Out of Office", parseDate("2025-01-17 00:00"), parseDate("2025-01-19 23:59")));

        events.add(new Event("Budget Review", parseDate("2025-01-20 10:00"), parseDate("2025-01-20 12:00")));
        events.add(new Event("Budget Review", parseDate("2025-01-20 10:00"), parseDate("2025-01-20 12:00")));
        events.add(new Event("Team Building Exercise", parseDate("2025-01-21 14:00"), parseDate("2025-01-21 17:00")));
        events.add(new Event("Annual Report Prep", parseDate("2025-01-22 09:00"), parseDate("2025-01-23 18:00")));

        events.add(new Event("Product Roadmap Review", parseDate("2025-01-24 11:00"), parseDate("2025-01-24 13:00")));
        events.add(new Event("Weekend Hackathon", parseDate("2025-01-25 08:00"), parseDate("2025-01-26 20:00")));

        events.add(new Event("Strategy Meeting", parseDate("2025-01-27 09:00"), parseDate("2025-01-27 10:30")));
        events.add(new Event("Industry Webinar", parseDate("2025-01-28 16:00"), parseDate("2025-01-28 18:00")));

        events.add(new Event("Quarterly Review", parseDate("2025-01-29 10:00"), parseDate("2025-01-29 12:00")));
        events.add(new Event("Team Lunch", parseDate("2025-01-30 12:30"), parseDate("2025-01-30 14:00")));
        events.add(new Event("Leadership Offsite", parseDate("2025-01-31 07:00"), parseDate("2025-02-02 19:00")));

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
    public List<Event> getEvents(){
        return getSampleEvents();
        //return new ArrayList<Event>();
    }
}
