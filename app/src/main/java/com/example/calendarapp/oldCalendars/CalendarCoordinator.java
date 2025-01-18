package com.example.calendarapp.oldCalendars;

import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarCoordinator {
    private final CalendarContainer container;
    private final AbstractCalendar calendar;
    private CalendarPagerAdapter adapter;

    public CalendarCoordinator(CalendarContainer container, AbstractCalendar calendar) {
        this.container = container;
        this.calendar = calendar;
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPager2 viewPager = container.getViewPager();
        List<Date> dateRange = getDateRange();
        adapter = new CalendarPagerAdapter(container.getContext(), calendar, dateRange);

        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                calendar.navigate(position - getMiddleIndex());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
                container.setTitle(dateFormat.format(calendar.getCurrentDate()));
            }
        });

        viewPager.setCurrentItem(getMiddleIndex(), false);
    }

    private List<Date> getDateRange() {
        List<Date> dates = new ArrayList<>();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.add(Calendar.DAY_OF_MONTH, -10); // Start 10 days back

        for (int i = 0; i < 21; i++) { // 21 days range (10 days before, today, 10 days after)
            dates.add(tempCalendar.getTime());
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }

    private int getMiddleIndex() {
        return 10; // Middle of the 21-day range
    }
}
