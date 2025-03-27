package com.example.calendarapp.Components;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.User;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

public class UserInfoComponent extends LinearLayout implements IComponent {
    public UserInfoComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public UserInfoComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public UserInfoComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public UserInfoComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
    }

    private void updateInfoUI(User user) {
        // Set layout orientation and padding
        this.setOrientation(HORIZONTAL);
        this.setPadding(16, 16, 16, 16);
        this.setBackgroundColor(Color.WHITE); // White background
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setElevation(8); // Add shadow effect (if API >= 21)
        this.setGravity(Gravity.CENTER_VERTICAL);
        Context context = getContext();
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

        // Add user display name (optional)
        TextView displayNameText = new TextView(context);
        displayNameText.setText("Name: " + user.name); // Assuming `user` has a `name` field
        displayNameText.setTextSize(14);
        displayNameText.setTextColor(Color.GRAY);
        displayNameText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        userDetailsLayout.addView(displayNameText);

        // Add user email
        TextView emailText = new TextView(context);
        emailText.setText("Email: " + user.email);
        emailText.setTextSize(16);
        emailText.setTextColor(Color.BLACK);
        emailText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        userDetailsLayout.addView(emailText);

        // Add the user details layout to the main component
        this.addView(userDetailsLayout);
    }

    public void updateInfo() {

        // Fetch user data from API
        API api = API.api();

        // Show user's info

        api.usersService.getMe().thenAccept(res ->{
            updateInfoUI(res.user);
        }).exceptionally(e ->{
            Log.e("UserInfoComponent", e.getMessage() + " ");
            Toast.makeText(getContext(),"User not found", Toast.LENGTH_SHORT).show();
            return null;
        });
    }
}
