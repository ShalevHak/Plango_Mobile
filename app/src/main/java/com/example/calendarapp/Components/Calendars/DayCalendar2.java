package com.example.calendarapp.Components.Calendars;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.calendarapp.Adapters.CalendarDayPagerAdapter;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayCalendar2 extends LinearLayout implements IComponent {

    private TextView tvDayOfMonth, tvMonth, tvYear;
    private ViewPager2 viewPager;
    private Calendar currentCalendar;
    private List<Date> visibleDates;
    private int pagerPos;

    private CalendarDayPagerAdapter adapter;
    public DayCalendar2(Context context) {
        super(context);
        initComponent(context);
    }

    public DayCalendar2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public DayCalendar2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public DayCalendar2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        currentCalendar = Calendar.getInstance();
        LinearLayout dayCalendar = (LinearLayout) inflate(context,R.layout.day_calendar_container,this);
        // Initialize TabLayout and ViewPager2
        tvDayOfMonth = dayCalendar.findViewById(R.id.tvDayOfMonth);
        tvMonth = dayCalendar.findViewById(R.id.tvMonth);
        tvYear = dayCalendar.findViewById(R.id.tvYear);
        viewPager = dayCalendar.findViewById(R.id.vpDays);
        pagerPos = 0;
        adapter = new CalendarDayPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        UpdateDateTitle();
        // Flag to suppress unnecessary callback triggers

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            boolean suppressCallback = false;
            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager2.SCROLL_STATE_IDLE && !suppressCallback) {
                    // Adjust the calendar and reset to the middle page
                    if (pagerPos == 0) { // Swiped to "previous day"
                        currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
                    } else if (pagerPos == 2) { // Swiped to "next day"
                        currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    // Update the title
                    UpdateDateTitle();

                    // Reset to page 1 without animation
                    suppressCallback = true;
                    viewPager.setCurrentItem(1, false);
                    suppressCallback= false;
                }
            }
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pagerPos = position; // Track the current page
            }
//            @Override
//            public void onPageSelected(int position) {
//                if (suppressCallback) return;
//                // Respond to page changes here
//                //TODO: update events
//                if(position>1){
//                    currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
//                }
//                if(position<1){
//                    currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
//                }
//                // Reset back to middle without animation
//                suppressCallback = true;
//                viewPager.setCurrentItem(1, false);
//                suppressCallback = false;
//                tvDayOfMonth.setText(currentCalendar.get(Calendar.DAY_OF_MONTH)+"");
//            }
        });
        // Link TabLayout with ViewPager2
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
//            tab.setText(dateFormat.format(currentCalendar.getTime()));
//        }).attach();
    }

    private void UpdateDateTitle() {
        String monthName = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(currentCalendar.getTime());
        tvMonth.setText(monthName);
        tvYear.setText(currentCalendar.get(Calendar.YEAR)+"");
        tvDayOfMonth.setText(currentCalendar.get(Calendar.DAY_OF_MONTH)+"");
    }
}
