package com.example.calendarapp.Fragments.Groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Activities.GroupActivity;
import com.example.calendarapp.Components.Calendars.DayCalendarComponent2;
import com.example.calendarapp.Managers.GroupsManager;
import com.example.calendarapp.R;

public class GroupCalendarFragment extends Fragment {
    private LinearLayout layout;

    private DayCalendarComponent2 calendarComponent;
    private GroupsManager groupsManager = GroupsManager.getInstance();
    private Group currGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_group_calendar, container, false);
        currGroup = GroupActivity.getCurrentGroup();
        bindViews(layout);
        setupUI();
        return layout;
    }

    private void bindViews(LinearLayout layout){
        String calendarId = DayCalendarComponent2.NONE;
        if(!currGroup.getCalendars().isEmpty())
            calendarId = currGroup.getCalendars().get(0).getId(); // Shows groups main calendar by default
        Log.i("GroupCalendarFragment","Binding views for calendar with id:" + calendarId);
        calendarComponent = new DayCalendarComponent2(getContext(), calendarId);
        layout.addView(calendarComponent);
    }
    private void setupUI() {
    }
}