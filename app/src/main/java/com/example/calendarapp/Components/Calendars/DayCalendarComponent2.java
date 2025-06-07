package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.calendarapp.Adapters.CalendarDayPagerAdapter;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Managers.CalendarsManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DayCalendarComponent2 extends LinearLayout implements IComponent {
    public static final String NONE = "NONE";
    private  String calendarId = null;
    private TextView tvDayOfMonth, tvMonth, tvYear;
    private ViewPager2 viewPager;
    private CalendarsManager calendarsManager;
    private int pagerPos;

    private CalendarDayPagerAdapter adapter;
    public DayCalendarComponent2(Context context, String calendarId) {
        super(context);
        this.calendarId = calendarId;
        initComponent(context);
    }

    public DayCalendarComponent2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public DayCalendarComponent2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public DayCalendarComponent2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        //Get an instance of CalendarService
        calendarsManager = CalendarsManager.getInstance();

        LinearLayout dayCalendar = (LinearLayout) inflate(context,R.layout.day_calendar_container,this);
        // Initialize TabLayout and ViewPager2
        tvDayOfMonth = dayCalendar.findViewById(R.id.tvDayOfMonth);
        tvMonth = dayCalendar.findViewById(R.id.tvMonth);
        tvYear = dayCalendar.findViewById(R.id.tvYear);
        viewPager = dayCalendar.findViewById(R.id.vpDays);

        pagerPos = 0;

        if (calendarId == null) {
            calendarsManager.getMyScheduleId().thenAccept(
                    id -> {
                        calendarId = id;
                        initViewPager();
                    }
            ).exceptionally(e ->{
                Toast.makeText(context, "Could not find user's schedule", Toast.LENGTH_SHORT).show();
                return null;
            });
        }
        initViewPager();
    }

    private void initViewPager() {
        Log.i("DayCalendarComponent2","Started init ViewPager for calendar with id: " + calendarId);
        adapter = new CalendarDayPagerAdapter(calendarId);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        updateDateTitle();
        setViewPagerListener();
    }

    private void setViewPagerListener() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            boolean ignoreNextStateChange = true; // Flag to suppress unnecessary callback triggers
            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager2.SCROLL_STATE_IDLE && !ignoreNextStateChange) {
                    // Adjust the calendar and reset to the middle page

                    // Update calendar based on Swiped direction
                    updateCalendar(pagerPos);

                    // Update the title
                    updateDateTitle();

                    // Reset to page 1 without animation and without unwanted recursive call to onPageScrollStateChanged
                    resetToMiddlePage();
                }
                if(ignoreNextStateChange) ignoreNextStateChange = false;
            }
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pagerPos = position; // Track the current page
            }
            private void resetToMiddlePage() {
                ignoreNextStateChange = true;
                viewPager.setCurrentItem(1, false);
                viewPager.getAdapter().notifyDataSetChanged();
            }

        });
    }

        private void updateCalendar(int pagerPos) {
            if (pagerPos == 0) { // Swiped to "previous day"
                calendarsManager.add(Calendar.DAY_OF_MONTH, -1);
            } else if (pagerPos == 2) { // Swiped to "next day"
                calendarsManager.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        private void updateDateTitle() {
        String monthName = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendarsManager.getCurrentCalendar().getTime());
        tvMonth.setText(monthName);
        tvYear.setText(calendarsManager.getCurrentCalendar().get(Calendar.YEAR)+"");
        tvDayOfMonth.setText(calendarsManager.getCurrentCalendar().get(Calendar.DAY_OF_MONTH)+"");
    }
}
