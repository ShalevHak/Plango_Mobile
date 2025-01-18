package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

public class CalendarContainer extends LinearLayout implements IComponent {
    private TextView dateTitle;
    private LinearLayout eventContainer;
    private Button prevButton, nextButton;

    public CalendarContainer(Context context) {
        super(context);
        initComponent(context);
    }

    public CalendarContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public CalendarContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public CalendarContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        LayoutInflater.from(context).inflate(R.layout.calendar_container_component, this);
        dateTitle = findViewById(R.id.dateTitle);
        eventContainer = findViewById(R.id.eventContainer);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
    }

    public void setTitle(String title) {
        dateTitle.setText(title);
    }

    public void addEvent(View eventView) {
        eventContainer.addView(eventView);
    }

    public void clearEvents() {
        eventContainer.removeAllViews();
    }

    public void setNavigationClickListener(OnClickListener prevListener, OnClickListener nextListener) {
        prevButton.setOnClickListener(prevListener);
        nextButton.setOnClickListener(nextListener);
    }

    public ViewPager2 getViewPager() {
        return findViewById(R.id.viewPager);
    }

}