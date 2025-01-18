package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.R;

import java.text.SimpleDateFormat;

public class EventComponent extends LinearLayout {
    private TextView tvEventTitle;
    private TextView tvEventTime;
    private Event event;

    public EventComponent(Context context) {
        super(context);
        init(context);
    }

    public EventComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Inflate the layout for an individual event
        LayoutInflater.from(context).inflate(R.layout.event_component, this, true);
        tvEventTitle = findViewById(R.id.tvEventTitle);
        tvEventTime = findViewById(R.id.tvEventTime);
    }

    public void setEvent(Event event) {
        this.event = event;
        tvEventTitle.setText(event.getTitle());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(event.getStartDate()) + " - " + timeFormat.format(event.getEndDate());
        tvEventTime.setText(time);
    }

    public Event getEvent() {
        return event;
    }
}
