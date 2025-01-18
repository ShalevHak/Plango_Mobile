package com.example.calendarapp.Components;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;

import com.example.calendarapp.API.API;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

public class UserComponent extends LinearLayout implements IComponent {
    public UserComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public UserComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public UserComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public UserComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        // Set layout orientation and padding
        this.setOrientation(HORIZONTAL);
        this.setPadding(16, 16, 16, 16);
        this.setBackgroundColor(Color.WHITE); // White background
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setElevation(8); // Add shadow effect (if API >= 21)
        this.setGravity(Gravity.CENTER_VERTICAL);

        // Fetch user data from API
        API api = API.api();

        // Check if user exists
//        if (api.usersService.user == null) {
//            throw new IllegalStateException("User is not initialized.");
//        }

        // Add user profile picture (optional placeholder)
        ImageView profileImage = new ImageView(context);
        profileImage.setLayoutParams(new LayoutParams(100, 100)); // Fixed size
        profileImage.setImageResource(R.drawable.user_icon); // Replace with a real profile picture or placeholder
        profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.addView(profileImage);

        // Add a vertical layout for user details
        LinearLayout userDetailsLayout = new LinearLayout(context);
        userDetailsLayout.setOrientation(VERTICAL);
        userDetailsLayout.setPadding(16, 0, 0, 0); // Padding between image and text
        userDetailsLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Add user email
        TextView emailText = new TextView(context);
        //emailText.setText("Email: " + api.usersService.user.email);
        emailText.setTextSize(16);
        emailText.setTextColor(Color.BLACK);
        emailText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        userDetailsLayout.addView(emailText);

        // Add user display name (optional)
        TextView displayNameText = new TextView(context);
        //displayNameText.setText("Name: " + api.usersService.user.name); // Assuming `user` has a `name` field
        displayNameText.setTextSize(14);
        displayNameText.setTextColor(Color.GRAY);
        displayNameText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        userDetailsLayout.addView(displayNameText);

        // Add the user details layout to the main component
        this.addView(userDetailsLayout);
    }
}
