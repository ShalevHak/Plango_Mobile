package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

public class DayComponent extends LinearLayout implements IComponent {

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
        LinearLayout day = findViewById(R.id.llDay);
        LinearLayout hourContainer = createHourContainer();

        // Add the hour container to the ScrollView
        day.addView(hourContainer);
    }

    private LinearLayout createHourContainer() {
        // Create LinearLayout to hold the 24-hour blocks
        LinearLayout hourContainer = new LinearLayout(getContext());
        hourContainer.setOrientation(LinearLayout.VERTICAL);
        hourContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        hourContainer.setPadding(8, 8, 8, 8);

        // Add all 24 hours dynamically
        for (int i = 0; i < 24; i++) {
            hourContainer.addView(createHourBlock(getContext(), i));
        }
        return hourContainer;
    }

    private LinearLayout createHourBlock(Context context, int hour) {
        // Create a container for each hour block
        LinearLayout hourBlock = new LinearLayout(context);
        hourBlock.setOrientation(LinearLayout.HORIZONTAL);
        hourBlock.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        hourBlock.setPadding(8, 8, 8, 8);
        hourBlock.setWeightSum(10);

        // Add hour label (e.g., "12 AM", "1 PM")
        TextView hourLabel = new TextView(context);
        hourLabel.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        hourLabel.setText(formatHour(hour));
        hourLabel.setTextSize(16);
        hourLabel.setTextColor(Color.BLACK);

        // Add event placeholder
        TextView eventPlaceholder = new TextView(context);
        eventPlaceholder.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                9));
        eventPlaceholder.setText("No Events");
        eventPlaceholder.setTextSize(14);
        eventPlaceholder.setTextColor(Color.GRAY);
        eventPlaceholder.setPadding(8, 0, 0, 0);
        eventPlaceholder.setBackgroundColor(Color.WHITE);

        // Add hour label and event placeholder to the hour block
        hourBlock.addView(hourLabel);
        hourBlock.addView(eventPlaceholder);

        return hourBlock;
    }
    private static String formatHour(int hour) {
        // Convert 24-hour format to 12-hour format with AM/PM
        String suffix = (hour < 12) ? "AM" : "PM";
        int hourIn12 = (hour % 12 == 0) ? 12 : hour % 12;
        return hourIn12 + " " + suffix;
    }


}
