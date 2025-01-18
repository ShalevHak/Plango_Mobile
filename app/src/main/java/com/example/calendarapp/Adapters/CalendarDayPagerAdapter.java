package com.example.calendarapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.Components.Calendars.DayComponent;
import com.example.calendarapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarDayPagerAdapter extends RecyclerView.Adapter<CalendarDayPagerAdapter.ViewHolder>{
    private Calendar calendar;
    public CalendarDayPagerAdapter() {
        calendar = Calendar.getInstance();
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
        holder.bind(position); // Populate content for the date
    }

    @Override
    public int getItemCount() {
        return 3;
    }
    public void updateCalendar(int offSet){
        calendar.add(Calendar.DAY_OF_MONTH, offSet);
    }
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private static int count;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(int pos) {
        }
    }
}
