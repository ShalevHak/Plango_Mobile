package com.example.calendarapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.Components.Calendars.DayComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Services.CalendarService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarDayPagerAdapter extends RecyclerView.Adapter<CalendarDayPagerAdapter.ViewHolder>{
    private CalendarService calendarService;
    public CalendarDayPagerAdapter() {
        calendarService = CalendarService.getInstance();
    }
    @NonNull
    @Override
    public CalendarDayPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DayComponent dayComponent = new DayComponent(parent.getContext());

        // Set layout parameters to MATCH_PARENT
        dayComponent.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));


        return new ViewHolder(dayComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarDayPagerAdapter.ViewHolder holder, int position) {
        List<Event> events = calendarService.GetCurrentDayEvent();
        holder.bind(position, events);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private static int count;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(int position, List<Event> events ) {
            ((DayComponent) itemView).clearEvents();
            if(position!= 1)return;
            Log.i("ADAPTER","binding view");
            ((DayComponent) itemView).resetScroller();
            ((DayComponent) itemView).addEvents(events);
        }
    }
}
