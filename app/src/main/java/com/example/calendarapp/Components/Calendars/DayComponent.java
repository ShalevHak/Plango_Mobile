package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.ThemeUtils;

import java.util.Calendar;
import java.util.List;

public class DayComponent extends LinearLayout implements IComponent {
    private LinearLayout hourBackground;
    private LinearLayout fullDayEventContainer;

    private FrameLayout eventContainer;
    private ScrollView svDay;
    private HorizontalScrollView hsvFullDayEvents;


    public DayComponent(Context context) {
        super(context);
        initComponent(context);
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
        hourBackground = findViewById(R.id.llHourBackground);
        eventContainer = findViewById(R.id.flEventContainer);
        svDay = findViewById(R.id.svDay);
        hsvFullDayEvents = findViewById(R.id.hsvFullDayEvents);
        fullDayEventContainer = findViewById(R.id.llFullDayEvents);

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
        for (Event event : events) {
            if(event.isFullDay()){
                addFullDayEventBlock(event);
            }
            else{
                addEventBlock(event);
            }

        }
    }
    private void addEventBlock(Event event) {
        EventComponent2 eventComponent = new EventComponent2(getContext());
        eventComponent.initEvent(event,200);
        eventContainer.addView(eventComponent);
    }
    private void addFullDayEventBlock(Event event){
        EventComponent2 eventComponent = new EventComponent2(getContext());
        eventComponent.initFullDayEvent(event);
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
}
