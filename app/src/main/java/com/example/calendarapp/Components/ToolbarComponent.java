package com.example.calendarapp.Components;


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
import com.example.calendarapp.API.Services.UsersService;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.Fragments.CalendarFragment;
import com.example.calendarapp.Fragments.GroupsFragment;
import com.example.calendarapp.Fragments.UserFragment;
import com.example.calendarapp.Fragments.UserSettingsFragment;
import com.example.calendarapp.Activities.MainActivity;
import com.example.calendarapp.R;

import java.lang.reflect.InvocationTargetException;

public class ToolbarComponent extends LinearLayout implements IComponent {
    private ImageButton btnUser, btnGroups, btnCalendar, btnLogout;
    private FrameLayout flFragmentContainer;
    private static Class<? extends Fragment> currentFragmentClass;
    private FragmentManager fragmentManager;
    private static int selectedBtnId;
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

    private final OnClickListener changeFragment = v -> {
        if (fragmentManager == null || flFragmentContainer == null) return;
        Class<? extends Fragment> clazz = null; // Initialize clazz with null
        int id = v.getId(); // Get the ID of the clicked view

        if (id == R.id.btnUser) {
            clazz = UserFragment.class;
        } else if (id == R.id.btnGroups) {
            clazz = GroupsFragment.class;
        } else if (id == R.id.btnCalendar) {
            clazz = CalendarFragment.class;
        }
        String tag = (String) getTag(); // Retrieve the tag

        setButtonGreyer((ImageButton)findViewById(id));
        // Perform the fragment transaction
        if (clazz != null) {
            try {
                selectedBtnId = id;
                currentFragmentClass = clazz;

                replaceFragment();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void replaceFragment() {
        try {
            // Default to User Info Fragment
            if(currentFragmentClass == null) currentFragmentClass = UserFragment.class;

            Fragment fragment = currentFragmentClass.getDeclaredConstructor().newInstance();



            // Replace the current fragment
            fragmentManager.beginTransaction()
                    .replace(flFragmentContainer.getId(), fragment)
                    .addToBackStack(null) // Optional: Adds the transaction to the back stack
                    .commit();
            // init the fragment
            if (fragment instanceof IComponent)
                ((IComponent) fragment).initComponent(fragment.getContext());
        }catch (Exception e){
            Log.e("Toolbar","Unable to start fragment: " + e.toString());
            e.printStackTrace();
        }


    }

    private void setButtonGreyer(ImageButton btn) {
        btn.setEnabled(false); // Disable the button to indicate it's inactive
        btn.setColorFilter(Color.parseColor("#AAEEEE")); // Semi-transparent grey color
        resetAllButtonsButOne(btn);
    }

    private void resetAllButtonsButOne(ImageButton activeButton) {
        // Assuming you have a list or array of all the buttons
        ImageButton[] allButtons = {btnUser, btnGroups, btnCalendar};

        for (ImageButton button : allButtons) {
            if (button != activeButton) {
                button.setEnabled(true); // Enable the button
                button.setColorFilter(null); // Reset to default color (null removes the filter)
            }
        }
    }


    @Override
    public void initComponent(Context context) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_component,this);
        btnUser = findViewById(R.id.btnUser);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnGroups = findViewById(R.id.btnGroups);
        btnLogout = findViewById(R.id.btnLogout);
        btnGroups.setOnClickListener(changeFragment);
        btnUser.setOnClickListener(changeFragment);
        btnCalendar.setOnClickListener(changeFragment);
        btnLogout.setOnClickListener(v-> logout());
        if(selectedBtnId == 0){
            setButtonGreyer(btnUser);
        }
        else{
            setButtonGreyer(findViewById(selectedBtnId));
        }
    }

    private void logout() {
        API.api().usersService.logout()
                .thenRun(()->{
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

    public void initFragmentManagement(FrameLayout flFragmentContainer,FragmentManager fragmentManager) {
        if(this.flFragmentContainer == null && this.fragmentManager == null){
            this.fragmentManager = fragmentManager;
            this.flFragmentContainer = flFragmentContainer;
        }

        replaceFragment();
    }

}
