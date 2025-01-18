package com.example.calendarapp.oldCalendars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarPagerAdapter extends RecyclerView.Adapter<CalendarPagerAdapter.ViewHolder> {
    private final List<Date> dates;
    private final AbstractCalendar calendar;
    private final Context context;

    public CalendarPagerAdapter(Context context, AbstractCalendar calendar, List<Date> dates) {
        this.context = context;
        this.calendar = calendar;
        this.dates = new ArrayList<>(dates);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date date = dates.get(position);
        List<Event> events = calendar.getEventsForDate(date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        holder.dateTitle.setText(dateFormat.format(date));

        for (Event event : events) {
            TextView eventView = new TextView(context);
            eventView.setText(event.getTitle());
            eventView.setPadding(10, 10, 10, 10);
            eventView.setBackgroundResource(R.drawable.event_item_background);
            holder.eventContainer.addView(eventView);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTitle;
        LinearLayout eventContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTitle = itemView.findViewById(R.id.dateTitle);
            eventContainer = itemView.findViewById(R.id.eventContainer);
        }
    }
}