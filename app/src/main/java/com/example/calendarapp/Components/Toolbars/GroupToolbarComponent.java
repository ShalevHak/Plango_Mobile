package com.example.calendarapp.Components.Toolbars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Activities.ContentActivity;
import com.example.calendarapp.Activities.GroupActivity;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.Fragments.Content.CalendarFragment;
import com.example.calendarapp.Fragments.Content.GroupsFragment;
import com.example.calendarapp.Fragments.Content.UserInfoFragment;
import com.example.calendarapp.Fragments.Groups.GroupInfoFragment;
import com.example.calendarapp.Fragments.Groups.GroupCalendarFragment;
import com.example.calendarapp.Fragments.Groups.GroupUpdatesFragment;
import com.example.calendarapp.R;

public class GroupToolbarComponent extends AbstractToolbarComponent implements IComponent {
    private ImageButton btnGroupInfo, btnGroupCalendar, btnGroupUpdates, btnGoBackFromGroup;


    public GroupToolbarComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public GroupToolbarComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public GroupToolbarComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public GroupToolbarComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }
    private final OnClickListener goBackToContentActivity = v -> {
        Intent intent = new Intent(this.getContext(), ContentActivity.class);
        getContext().startActivity(intent);
    };
    @Override
    protected void resetAllButtonsBut(ImageButton activeButton) {
        ImageButton[] allButtons = {btnGroupInfo, btnGroupCalendar, btnGroupUpdates};
        for (ImageButton button : allButtons) {
            if (button != activeButton) {
                button.setEnabled(true);
                button.setColorFilter(null);
            }
        }
    }

    @Override
    protected Class<? extends Fragment> getFragmentClassForButtonId(int id) {
        if (id == R.id.btnGroupInfo) return GroupInfoFragment.class;
        if (id == R.id.btnGroupCalendar) return GroupCalendarFragment.class;
        if (id == R.id.btnGroupUpdates) return GroupUpdatesFragment.class;
        return GroupInfoFragment.class;
    }

    @Override
    protected Class<? extends Fragment> getDefaultFragmentClass() {
        return GroupInfoFragment.class;
    }

    @Override
    public void initComponent(Context context) {
        LayoutInflater.from(context).inflate(R.layout.group_toolbar_component, this);
        btnGroupInfo = findViewById(R.id.btnGroupInfo);
        btnGroupCalendar = findViewById(R.id.btnGroupCalendar);
        btnGroupUpdates = findViewById(R.id.btnGroupUpdates);
        btnGoBackFromGroup = findViewById(R.id.btnGoBackFromGroup);

        btnGroupInfo.setOnClickListener(changeFragment);
        btnGroupCalendar.setOnClickListener(changeFragment);
        btnGroupUpdates.setOnClickListener(changeFragment);
        btnGoBackFromGroup.setOnClickListener(goBackToContentActivity);

        if (selectedBtnId == 0) {
            setButtonGreyer(btnGroupInfo);
        } else {
            setButtonGreyer(findViewById(selectedBtnId));
        }
    }
}
