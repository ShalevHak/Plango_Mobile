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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.calendarapp.API.API;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.Fragments.Content.CalendarFragment;
import com.example.calendarapp.Fragments.Content.GroupsFragment;
import com.example.calendarapp.Fragments.Content.UserInfoFragment;
import com.example.calendarapp.Activities.MainActivity;
import com.example.calendarapp.R;

public class ToolbarComponent extends AbstractToolbarComponent implements IComponent {
    private ImageButton btnUser, btnGroups, btnCalendar, btnLogout;
    public ToolbarComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public ToolbarComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public ToolbarComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    @Override
    protected Class<? extends Fragment> getFragmentClassForButtonId(int id) {
        if (id == R.id.btnUser) return UserInfoFragment.class;
        if (id == R.id.btnGroups) return GroupsFragment.class;
        if (id == R.id.btnCalendar) return CalendarFragment.class;
        return null;
    }

    @Override
    protected Class<? extends Fragment> getDefaultFragmentClass() {
        return UserInfoFragment.class;
    }

    @Override
    protected void resetAllButtonsBut(ImageButton activeButton) {
        ImageButton[] buttons = {btnUser, btnGroups, btnCalendar};
        for (ImageButton button : buttons) {
            if (button != activeButton) {
                button.setEnabled(true);
                button.setColorFilter(null);
            }
        }
    }


    @Override
    public void initComponent(Context context) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_component, this);
        btnUser = findViewById(R.id.btnUser);
        btnGroups = findViewById(R.id.btnGroups);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnLogout = findViewById(R.id.btnLogout);

        btnUser.setOnClickListener(changeFragment);
        btnGroups.setOnClickListener(changeFragment);
        btnCalendar.setOnClickListener(changeFragment);
        btnLogout.setOnClickListener(v -> logout());

        if (selectedBtnId == 0) {
            setButtonGreyer(btnUser);
        } else {
            setButtonGreyer(findViewById(selectedBtnId));
        }
    }

    private void logout() {
        API.api().usersService.logout()
                .thenRun(()->{
                    currentFragmentClass = null;
                    Log.i("Toolbar","Log out was successful");
                    navigateToMainActivity();
                })
                .exceptionally(e -> {
            Log.e("Toolbar","Log out error: " + e.getMessage());
            return null;
        });


    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }

}
