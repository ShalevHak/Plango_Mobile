
package com.example.calendarapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.R;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {

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


}

