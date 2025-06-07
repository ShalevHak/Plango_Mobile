package com.example.calendarapp.Components.Calendars;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.example.calendarapp.Utils.ErrorUtils;
import com.example.calendarapp.Utils.ThemeUtils;
import com.example.calendarapp.Utils.TimeUtils;
import com.example.calendarapp.Notifications.NotificationUtils;
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
    private Button btnExtras;
    private AlertDialog overflowDialog = null;

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
        btnExtras              = findViewById(R.id.btnExtras);

        btnExtras.setVisibility(GONE); // start hidden


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
        hourBackground.removeAllViews();
        int hourHeight = dp(64); // more compact, feel free to tune
        int textPaddingTop = dp(4);

        for (int i = 0; i < 24; i++) {
            FrameLayout hourBlock = new FrameLayout(getContext());
            hourBlock.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, hourHeight));

            // Divider (top line)
            View divider = new View(getContext());
            FrameLayout.LayoutParams dividerParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, dp(1));
            dividerParams.gravity = Gravity.TOP;
            divider.setLayoutParams(dividerParams);
            divider.setBackgroundColor(Color.parseColor("#AAAAAA"));

            // Hour label
            TextView hourLabel = new TextView(getContext());
            hourLabel.setText(formatHour(i));
            hourLabel.setTextColor(ThemeUtils.resolveColorFromTheme(getContext(), R.attr.colorPrimaryText));
            hourLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            hourLabel.setPadding(dp(8), textPaddingTop, 0, 0);
            hourLabel.setGravity(Gravity.START);
            hourLabel.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.TOP | Gravity.START
            ));

            hourBlock.addView(divider);
            hourBlock.addView(hourLabel);
            hourBackground.addView(hourBlock);
        }
    }


    private int dp(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void addEvents(List<Event> events) {
        if (overflowDialog != null && overflowDialog.isShowing()) {
            overflowDialog.dismiss();
            overflowDialog = null;
        }
        btnExtras.setVisibility(GONE);
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

        List<Event> overflowEvents = uiDayEvents.stream().filter((UIDayEvent e)->e.isExtra).map(ui -> ui.event).collect(Collectors.toList());

        uiDayEvents.removeIf((UIDayEvent e)->e.isExtra);
        // 4) Add each to the ConstraintLayout
        for (UIDayEvent uiDayEvent : uiDayEvents) {
            addUIDayEventBlock(uiDayEvent);
        }

        // If extras exist, add a button to open the overflow dialog
        if (!overflowEvents.isEmpty()) {
            initOverflowEventsBtn(overflowEvents);
        }

    }
    private void initOverflowEventsBtn(List<Event> overflowEvents) {
        // Set up text and styling
        btnExtras.setText("Show " + overflowEvents.size() + " more");
        btnExtras.setAllCaps(false);
        btnExtras.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnExtras.setTextColor(ThemeUtils.resolveColorFromTheme(getContext(), R.attr.colorPrimaryText));
        btnExtras.setBackgroundColor(ThemeUtils.resolveColorFromTheme(getContext(), R.attr.colorBackgroundSecondary));
        btnExtras.setPadding(dp(8), dp(4), dp(8), dp(4));

        // Click shows a dialog of overflow events
        btnExtras.setOnClickListener(v -> showOverflowDialog(overflowEvents));

        // Margins and visibility
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp(8), dp(8), dp(8), dp(8));
        btnExtras.setLayoutParams(params);

        btnExtras.setVisibility(VISIBLE);
    }
    private void showOverflowDialog(List<Event> extras) {
        // If already showing, don't create a new one
        if (overflowDialog != null && overflowDialog.isShowing()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Overflow Events");

        ScrollView scrollView = new ScrollView(getContext());
        LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(16), dp(16), dp(16), dp(16));

        int margin = dp(4);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(margin, margin, margin, margin);

        for (Event event : extras) {
            EventComponent2 eventComponent = new EventComponent2(getContext());
            eventComponent.initOverflowEvent(event);
            eventComponent.initParentDay(this);
            container.addView(eventComponent, params);
        }

        scrollView.addView(container);
        builder.setView(scrollView);
        builder.setPositiveButton("Close", null);

        overflowDialog = builder.create();
        overflowDialog.show();
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
        final int minIntervals = 5;
        final int totalRows = (24 * 60) / minIntervals;
        int[] rowsOverlaps = new int[totalRows];

        for (int minute = 0; minute < 24 * 60; minute += minIntervals) {
            int intervalStart = minute;
            int intervalEnd = minute + minIntervals;

            long count = events.stream()
                    .filter(event -> {
                        int eventStart = getMinuteOfDay.apply(event.getStartDate());
                        int eventEnd = getMinuteOfDay.apply(event.getEndDate());
                        // An event overlaps the interval if it starts before the block ends
                        // and ends after the block starts.
                        return eventStart < intervalEnd && eventEnd > intervalStart;
                    })
                    .count();
            rowsOverlaps[minute/minIntervals]= (int) count;
        }

        // Helper lambda: event to rows range
        Function<Event, IntStream> getRowsRange = event ->{
            int eventStart = getMinuteOfDay.apply(event.getStartDate());
            int eventEnd = getMinuteOfDay.apply(event.getEndDate());
            int startRow = eventStart / minIntervals;
            // Subtract 1 minute from eventEnd so that an event ending exactly at the boundary is included in the previous row.
            int endRow = (eventEnd - 1) / minIntervals;
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
        if(calendarId == null) return;
        calendarsManager.addEvent(event, calendarId)
                .thenAccept(e ->{
                    NotificationUtils.scheduleEventReminder(getContext(), event);
                    clearEvents();
                    displayEvents();
                })
                .exceptionally(
                e -> {
                    String msg = "Unable to add event: \n" + ErrorUtils.getCause(e); // TODO: e msg removal
                    Log.e("DayComponent",msg);
                    Toast.makeText(getContext(), msg,Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    public void editEvent(Event originalEvent, Event editedEvent) {
        if(calendarId == null) return;
        calendarsManager.updateEvent(originalEvent, editedEvent, calendarId)
                .thenAccept(e ->{
                    Log.i("DayComponent","Event Update Succeeded");
                    clearEvents();
                    displayEvents();
                })
                .exceptionally(
                e -> {
                    String msg = "Unable to update event: \n" +  ErrorUtils.getCause(e);  // TODO: e msg removal
                    Log.e("DayComponent",msg);
                    Toast.makeText(getContext(), msg,Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    public void displayEvents(){
        if(calendarId == null) return;
        calendarsManager.GetCurrentDayEvent(calendarId).thenAccept(this::addEvents).exceptionally(e -> {
            String msg = "Unable to load events: \n" + ErrorUtils.getCause(e); //TODO: remove error msgs from toasts to user.
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
