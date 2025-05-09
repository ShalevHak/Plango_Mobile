package com.example.calendarapp.Components.Calendars;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Managers.CalendarsManager;
import com.example.calendarapp.Utils.ThemeUtils;
import com.example.calendarapp.Utils.TimeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DayComponent extends LinearLayout implements IComponent {
    private LinearLayout hourBackground;
    private LinearLayout fullDayEventContainer;

    private ConstraintLayout eventContainer;
    private ScrollView svDay;
    private HorizontalScrollView hsvFullDayEvents;
    private CalendarsManager calendarsManager;
    private FloatingActionButton fabAddEvent;
    private String calendarId;

    public DayComponent(Context context, String calendarId) {
        super(context);
        initComponent(context);
        this.calendarId = calendarId;
    }

    public DayComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public DayComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public DayComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }
    @Override
    public void initComponent(Context context) {
        inflate(context, R.layout.day_component, this);

        calendarsManager = CalendarsManager.getInstance();

        hourBackground         = findViewById(R.id.llHourBackground);
        eventContainer         = findViewById(R.id.clEventContainer);
        svDay                  = findViewById(R.id.svDay);
        hsvFullDayEvents       = findViewById(R.id.hsvFullDayEvents);
        fullDayEventContainer  = findViewById(R.id.llFullDayEvents);
        fabAddEvent            = findViewById(R.id.fabAddEvent);

        // Click Listener for Adding Events
        fabAddEvent.setOnClickListener(view -> {
            // Get the current date from the calendar service
            Date now = calendarsManager.GetCurrentDay();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(now);

            // Round to the nearest hour
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);

            // Set the end time 1 hour later
            Calendar endCalendar = (Calendar) startCalendar.clone();
            endCalendar.add(Calendar.HOUR_OF_DAY, 1);

            // Create a new event with the rounded time
            Event newEvent = new Event("", startCalendar.getTime(), endCalendar.getTime());

            // Open the event editor with this default event
            EventEditComponent.openEditEventDialog(getContext(), newEvent, this::addNewEvent);
        });


        // Clear previous
        hourBackground.removeAllViews();
        eventContainer.removeAllViews();
        fullDayEventContainer.removeAllViews();

        // Add the hour background to the ScrollView
        createHourBackground();
    }
    private void createHourBackground() {
        for (int i = 0; i < 24; i++) {
            TextView hourLabel = new TextView(getContext());
            hourLabel.setText(formatHour(i));
            hourLabel.setPadding(16, 32, 16, 32);  // Taller for visibility
            hourLabel.setBackgroundColor(ThemeUtils.resolveColorFromTheme(getContext(),R.attr.colorBackground));
            hourLabel.setTextColor(ThemeUtils.resolveColorFromTheme(getContext(),R.attr.colorPrimaryText));
            hourLabel.setGravity(Gravity.CENTER_VERTICAL);
            hourLabel.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200)); // Fixed height for consistent scaling

            hourBackground.addView(hourLabel);
        }
    }
    public void addEvents(List<Event> events) {
        if (events == null || events.isEmpty()) return;

        // 1) Separate full-day events from time-based events
        List<Event> fullDay = new ArrayList<>();
        List<Event> timed   = new ArrayList<>();
        for (Event e : events) {
            if (e.isFullDay()) fullDay.add(e);
            else               timed.add(e);
        }

        // 2) Add full-day events to the horizontal container
        for (Event fdEvent : fullDay) {
            addFullDayEventBlock(fdEvent);
        }

        // 3) Organize normal events into UIDayEvents
        List<UIDayEvent> uiDayEvents = organizeEvents(timed);
        if (uiDayEvents == null) return;

        List<UIDayEvent> extras = uiDayEvents.stream().filter((UIDayEvent e)->e.isExtra).collect(Collectors.toList());


        uiDayEvents.removeIf((UIDayEvent e)->e.isExtra);
        // 4) Add each to the ConstraintLayout
        for (UIDayEvent uiDayEvent : uiDayEvents) {
            addUIDayEventBlock(uiDayEvent);
        }
    }


    // ----------------------------------------------------------------------------
    // ------------------- The Overlap/Organize Algorithm -------------------------
    // ----------------------------------------------------------------------------

    private List<UIDayEvent> organizeEvents(List<Event> events){
        // Algorithm structure:
        // Each event requires x,y,w,h values for position (By percentage weights):
        // * x - horizontal margin (left margin) - calculated using the Greedy algorithm
        // * y - vertical margin (top margin) - calculated based on start hour
        // * w - width - calculated by max overlap row of an event
        // * h - height - calculated based on event's duration
        // Flow of the algorithm:
        // 1) Sorting the Event list by start date and by end date.
        // 2) Mapping each event to a corresponding width weight.
        // 3) Greedy algorithm:
        //      > loop over the list of event until every event is mapped to a left margin (x position).
        //      > Col by col place events while non-overlapping with last placed event. Keep The left margin at each row.
        // 4) Calculate the height and the top margin of each event
        //    based on the start time and the duration.

        if(events.isEmpty()) return null;

        // comparator for sorting based on start and end dates
        Comparator<Event> eventComparator = Comparator
                .comparing(Event::getStartDate)  // First, compare by start time
                .thenComparing(Event::getEndDate);  // Then, by end time

        // Apply sorting
        events.sort(eventComparator);

        //---------------Mapping events to widths ---------------

        // > calculate overlaps by row: count how many overlapping events are there for every 5 minutes interval

        // Helper lambda: convert a Date to its minute-of-day
        Function<Date, Integer> getMinuteOfDay = TimeUtils::getMinuteOfDay;

        // Calculate number of events in each row (5 minutes intervals)
        int totalRows = (24 * 60) / 5;
        int[] rowsOverlaps = new int[totalRows];

        for (int minute = 0; minute < 24 * 60; minute += 5) {
            int intervalStart = minute;
            int intervalEnd = minute + 5;

            long count = events.stream()
                    .filter(event -> {
                        int eventStart = getMinuteOfDay.apply(event.getStartDate());
                        int eventEnd = getMinuteOfDay.apply(event.getEndDate());
                        // An event overlaps the interval if it starts before the block ends
                        // and ends after the block starts.
                        return eventStart < intervalEnd && eventEnd > intervalStart;
                    })
                    .count();
            rowsOverlaps[minute/5]= (int) count;
        }

        // Helper lambda: event to rows range
        Function<Event, IntStream> getRowsRange = event ->{
            int eventStart = getMinuteOfDay.apply(event.getStartDate());
            int eventEnd = getMinuteOfDay.apply(event.getEndDate());
            int startRow = eventStart / 5;
            // Subtract 1 minute from eventEnd so that an event ending exactly at the boundary is included in the previous row.
            int endRow = (eventEnd - 1) / 5;
            // Use IntStream to get the range

            return IntStream.rangeClosed(startRow, endRow);
        };

        // > Calculate the weight of each event based on the max overlaps it has in a single row
        Map<Event, Float> widthWeights = events.stream()
                .collect(Collectors.toMap(
                        event -> event,
                        event -> {
                            // Use getRowsRange to get the max value in rowsOverlaps between startRow and endRow (inclusive)
                            return 1f /getRowsRange.apply(event)
                                    .map(row -> rowsOverlaps[row])
                                    .max()
                                    .orElse(1);
                        }
                ));

        // Calculate left margins for each event Based on greedy placement
        Map<Event, Float> leftMarginWeights = new HashMap<>();

        float[] leftMarginByRow = new float[totalRows];

        //Helper lambda: get the required left margin for an event
        Function<Event,Float>  getMaxLeftMarginForEvent = event ->
                (float) getRowsRange.apply(event).mapToDouble(row ->
                                leftMarginByRow[row])
                        .max()
                        .orElse(0f);

        //Helper lambda: set left margin for the range of an event
        Function<Event,Void>  updateLeftMarginByRow = event -> {
            float updatedMargin = getMaxLeftMarginForEvent.apply(event) + widthWeights.getOrDefault(event,0f);
            getRowsRange.apply(event).forEach(row ->
                leftMarginByRow[row] = updatedMargin
            );
            return null;
        };

        //TODO: deal with extras!
        List<Event> extras = new ArrayList<>();

        List<Event> copy = new ArrayList<>(events); // Mutable copy

        //Remove first
        final Event first = copy.remove(0);
        leftMarginWeights.put(first,0f);
        updateLeftMarginByRow.apply(first);
        int lastEndMinute = getMinuteOfDay.apply(first.getEndDate());

        while (!copy.isEmpty()) {
            for (Iterator<Event> iterator = copy.iterator(); iterator.hasNext();) {
                Event event = iterator.next();

                // Check for overlap
                if (getMinuteOfDay.apply(event.getStartDate()) < lastEndMinute) continue;

                iterator.remove(); // Use iterator.remove() to avoid ConcurrentModificationException

                float leftMargin = getMaxLeftMarginForEvent.apply(event);

                //TODO: limit the max number of columns to 3

                // Weight is over the limit. Will cause the event to be positioned out side of the screen
                if (leftMargin + widthWeights.getOrDefault(event,0f) > 1) {
                    extras.add(event);
                    continue;
                }

                leftMarginWeights.put(event, leftMargin);
                updateLeftMarginByRow.apply(event);

                lastEndMinute = getMinuteOfDay.apply(event.getEndDate());
            }
            lastEndMinute = 0;
        }

        // Helper lambda: get height weight based on event duration
        Function<Event,Float> getHeightWeight = (Event event) ->
                Math.max(0.01f, (float)((getMinuteOfDay.apply(event.getEndDate()) - getMinuteOfDay.apply(event.getStartDate()))) / (24 * 60));

        // helper lambda: get top margin weight;
        Function<Event, Float> getTopMarginWeight = event ->
                (float)(getMinuteOfDay.apply(event.getStartDate()) )/ (24f * 60);

        List<UIDayEvent> uiDayEvents = new ArrayList<>();
        for(Event event : events) {
            if(extras.contains(event)) {
                Log.i("DayComponent", "Overflow event added");
                uiDayEvents.add(new UIDayEvent(event,true));
                continue;
            }

            float width = (float) widthWeights.getOrDefault(event,1f);
            float height = (float) getHeightWeight.apply(event);

            float leftMargin = (float) leftMarginWeights.getOrDefault(event,1f);
            float topMargin = (float) getTopMarginWeight.apply(event);


            uiDayEvents.add(new UIDayEvent(event,
                    width,
                    height,
                    leftMargin,
                    topMargin));
        }
        return  uiDayEvents;
    }

//    private void addEventBlock(Event event) {
//        EventComponent2 eventComponent = new EventComponent2(getContext());
//        eventComponent.initEvent(event,200);
//        eventContainer.addView(eventComponent);
//    }

    private void  addUIDayEventBlock(UIDayEvent event){
        //TODO: deal with extras!
        if(event.isExtra)return;
        EventComponent2 eventComponent = new EventComponent2(getContext());
        eventComponent.initOrganizedEvent(event);
        eventComponent.initParentDay(this);

        eventContainer.addView(eventComponent);
    }
    private void addFullDayEventBlock(Event event){
        EventComponent2 eventComponent = new EventComponent2(getContext());
        eventComponent.initFullDayEvent(event);
        eventComponent.initParentDay(this);
        fullDayEventContainer.addView(eventComponent);
    }
    private static String formatHour(int hour) {
        // Convert 24-hour format to 12-hour format with AM/PM
        String suffix = (hour < 12) ? "AM" : "PM";
        int hourIn12 = (hour % 12 == 0) ? 12 : hour % 12;
        return hourIn12 + " " + suffix;
    }


    public void clearEvents() {
        eventContainer.removeAllViews();
        fullDayEventContainer.removeAllViews();
    }

    public void resetScroller() {
        svDay.scrollTo(0, 0);
    }
    private void addNewEvent(Event event) {
        calendarsManager.addEvent(event, calendarId)
                .thenAccept(e ->{
                    clearEvents();
                    displayEvents();
                })
                .exceptionally(
                e -> {
                    String msg = "Unable to add event: \n" + e.getMessage();
                    Log.e("DayComponent",msg);
                    Toast.makeText(getContext(), msg,Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    public void editEvent(Event originalEvent, Event editedEvent) {
        calendarsManager.updateEvent(originalEvent, editedEvent, calendarId)
                .thenAccept(e ->{
                    clearEvents();
                    displayEvents();
                })
                .exceptionally(
                e -> {
                    String msg = "Unable to update event: \n" + e.getMessage();
                    Log.e("DayComponent",msg);
                    Toast.makeText(getContext(), msg,Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    public void displayEvents(){
        calendarsManager.GetCurrentDayEvent().thenAccept(this::addEvents).exceptionally(e -> {
            String msg = "Unable to load events: \n" + e.getMessage();
            Log.e("DayComponent",msg);
            Toast.makeText(getContext(), msg,Toast.LENGTH_LONG).show();
            return null;
        });
    }
    static class UIDayEvent{
        public float widthWeight;
        public float heightWeight;
        public float leftMarginWeight;
        public float topMarginWeight;

        public Event event;
        public boolean isExtra;

        public UIDayEvent(Event event, float widthWeight, float heightWeight, float leftMarginWeight, float topMarginWeight) {
            this.widthWeight = widthWeight;
            this.heightWeight = heightWeight;
            this.leftMarginWeight = leftMarginWeight;
            this.topMarginWeight = topMarginWeight;
            this.event = event;
            this.isExtra = false;
        }

        public UIDayEvent(Event event, boolean isExtra) {
            this.isExtra = isExtra;
            this.event = event;
            Log.d("UIDayEvent", "Overflow event");
        }

        @Override
        public String toString() {
            return "UIDayEvent{" +
                    "\n widthWeight=" + widthWeight +
                    "\n heightWeight=" + heightWeight +
                    "\n leftMarginWeight=" + leftMarginWeight +
                    "\n topMarginWeight=" + topMarginWeight +
                    "\n event=" + event +
                    "\n isExtra=" + isExtra +
                    "\n}";
        }
    }
}
