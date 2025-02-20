package com.example.calendarapp.oldCalendars;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.ThemeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class CalendarComponent extends CustomContainerComponent implements IComponent {
    private Calendar currentCalendar;
    private TextView dateTitle;
    private List<Event> events;
    private GestureDetector gestureDetector;

    public CalendarComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public CalendarComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public CalendarComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public CalendarComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        currentCalendar = Calendar.getInstance();

        populateEvents();

        initTitle();
        updateDateTitle();
        displayEvents();
    }
    private void initTitle() {
        initPrevBtn();

        initTextViewDate();

        initNextBtn();
    }
    private void initPrevBtn() {
        initNavigationBtn("<",-1);
    }

    private void initNextBtn() {
        initNavigationBtn(">",1);
    }

    private void initNavigationBtn(String text,int amount){
        //TODO: improve btn design
        Button btn = new Button(getContext());
        btn.setText(text);
        int btnTextColor = ThemeUtils.resolveColorFromTheme(getContext(), R.attr.colorSecondaryText);
        btn.setTextColor(btnTextColor);
        int btnColor = ThemeUtils.resolveColorFromTheme(getContext(), R.attr.btnGoogleColorMid2);
        btn.setBackgroundColor(btnColor); // Example: Material Design primary color
        btn.setPadding(20, 10, 20, 10);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        btn.setLayoutParams(params);
        containerTitle.addView(btn);
        btn.setOnClickListener(v -> {
            currentCalendar.add(Calendar.DAY_OF_MONTH, amount);
            updateDateTitle();
            displayEvents();
        });
    }
    private void initTextViewDate() {
        dateTitle = new TextView(getContext());
        dateTitle.setGravity(Gravity.CENTER);
        dateTitle.setTextSize(18);
        containerTitle.addView(dateTitle);
    }

    private void updateDateTitle() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd");
        dateTitle.setText(dateFormat.format(currentCalendar.getTime()));
    }

    private void populateEvents(){
        events = new ArrayList<>();
        // Sample data
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
        //TODO: use API to fetch events
    }
    private void displayEvents() {
        scrollerContent.removeAllViews(); // Clear existing events

        Date today = currentCalendar.getTime();

        List<Event> fullDayEvents = filterEvents(today, true);
        List<Event> dailyEvents = filterEvents(today, false);

        dailyEvents.sort(this::compareEvents);

        displayFullDayEvents(fullDayEvents);

        displayTimedEvents(dailyEvents);
    }
    private List<Event> filterEvents(Date today, boolean fullDayOnly) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (isEventOnDate(event, today) && event.isFullDay() == fullDayOnly) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    private int compareEvents(Event event1, Event event2) {
        if (event1.getStartDate().after(event2.getStartDate())) {
            return 1;
        } else if (event1.getStartDate().before(event2.getStartDate())) {
            return -1;
        } else {
            return event1.getEndDate().compareTo(event2.getEndDate());
        }
    }

    private void displayFullDayEvents(List<Event> fullDayEvents) {
        for (Event event : fullDayEvents) {
            TextView fullDayLabel = new TextView(getContext());
            fullDayLabel.setText(event.getTitle());
            fullDayLabel.setTextSize(16);
            int color = ThemeUtils.resolveColorFromTheme(getContext(),R.attr.colorSecondaryText);
            fullDayLabel.setTextColor(color);
            fullDayLabel.setPadding(10, 10, 10, 10);
            fullDayLabel.setBackgroundResource(R.drawable.event_item_background);

            scrollerContent.addView(fullDayLabel);

        }
        if(!fullDayEvents.isEmpty()) addDivider(4);
    }

    private void displayTimedEvents(List<Event> timedEvents) {
        for (Event event : timedEvents) {
            EventComponent eventComponent = new EventComponent(getContext());
            eventComponent.setEvent(event);
            styleEventComponent(eventComponent, event);
            scrollerContent.addView(eventComponent);
            addDivider(2);
        }
    }

    private void addDivider(int thickness ) {
        View divider = new View(getContext());

        divider.setBackgroundColor(ThemeUtils.resolveColorFromTheme(getContext(),R.attr.colorDivider));


        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
        layoutParams.setMargins(0, 2, 0, 2);
        divider.setLayoutParams(layoutParams);
        scrollerContent.addView(divider);
    }


    private void styleEventComponent(EventComponent eventComponent, Event event) {
        // Dynamically set height based on event duration
        long durationInMinutes = (event.getEndDate().getTime() - event.getStartDate().getTime()) / (60 * 1000);
        int minHeight = 200; // Minimum height for short events
        int dynamicHeight = (int) Math.max(minHeight, durationInMinutes * 2); // Scale height by duration

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dynamicHeight);
        layoutParams.setMargins(0, 5, 0, 5);
        eventComponent.setLayoutParams(layoutParams);
        // Apply custom styling to improve design
        eventComponent.setPadding(20, 20, 20, 20);

        eventComponent.setBackgroundResource(R.drawable.event_item_background);
    }

    private boolean isEventOnDate(Event event, Date date) {
        Calendar eventStart = Calendar.getInstance();
        eventStart.setTime(event.getStartDate());

        Calendar eventEnd = Calendar.getInstance();
        eventEnd.setTime(event.getEndDate());

        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);

        // Check if the event starts or ends within the current day
        return (eventStart.before(todayEnd) && eventEnd.after(todayStart));
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
