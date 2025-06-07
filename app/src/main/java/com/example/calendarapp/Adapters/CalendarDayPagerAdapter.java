package com.example.calendarapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.Components.Calendars.DayComponent;
import com.example.calendarapp.Managers.CalendarsManager;


public class CalendarDayPagerAdapter extends RecyclerView.Adapter<CalendarDayPagerAdapter.ViewHolder>{
    private CalendarsManager calendarsManager;
    private Context parentContext;
    private String calendarId;
    public CalendarDayPagerAdapter(String calendarId) {
        calendarsManager = CalendarsManager.getInstance();
        this.calendarId = calendarId;
    }
    @NonNull
    @Override
    public CalendarDayPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContext = parent.getContext();
        DayComponent dayComponent = new DayComponent(parentContext, calendarId);

        // Set layout parameters to MATCH_PARENT
        dayComponent.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));


        return new ViewHolder(dayComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarDayPagerAdapter.ViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static int count;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(int position) {
            ((DayComponent) itemView).clearEvents();
            if(position!= 1)return;
            Log.i("CalendarDayPagerAdapter","binding view");
            ((DayComponent) itemView).resetScroller();
            ((DayComponent) itemView).displayEvents();
        }
    }
}
