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

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_component, parent, false);
        LinearLayout day = view.findViewById(R.id.llDay);
        // Create LinearLayout to hold the 24-hour blocks
        LinearLayout hourContainer = new LinearLayout(parent.getContext());
        hourContainer.setOrientation(LinearLayout.VERTICAL);
        hourContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        hourContainer.setPadding(8, 8, 8, 8);

        // Add all 24 hours dynamically
        for (int i = 0; i < 24; i++) {
            hourContainer.addView(createHourBlock(parent.getContext(), i));
        }

        // Add the hour container to the ScrollView
        day.addView(hourContainer);
        return new ViewHolder(view);
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
    static class ViewHolder extends RecyclerView.ViewHolder {
        private static int count;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(int pos) {
        }
    }
}
