package com.example.calendarapp.Components;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.User;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.function.BiFunction;

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
        Context context = getContext();
        removeAllViews(); // Clear previous views

        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        scrollView.setPadding(32, 32, 32, 32);
        scrollView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        scrollView.addView(container);
        this.addView(scrollView);

        // Utility to create readonly TextInput field
        BiFunction<String, String, View> createField = (label, value) -> {
            TextInputLayout layout = new TextInputLayout(context, null, com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            layout.setHint(label);
            layout.setBoxStrokeColor(ContextCompat.getColor(context, R.color.backgroundLightSecondary)); // optional
            layout.setBoxBackgroundColor(Color.TRANSPARENT);

            TextInputEditText input = new TextInputEditText(context);
            input.setText(value);
            input.setEnabled(false);
            input.setFocusable(false);
            input.setCursorVisible(false);
            input.setTextColor(Color.DKGRAY);
            input.setTextSize(16);
            layout.addView(input);
            return layout;
        };

        // Add fields
        container.addView(createField.apply("Name", user.getName()));
        container.addView(createField.apply("Email", user.getEmail()));
        if (user.getAbout() != null && !user.getAbout().isEmpty()) {
            container.addView(createField.apply("About", user.getAbout()));
        }

        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
        imageParams.setMargins(0, 32, 0, 32);
        imageParams.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(imageParams);
        imageView.setImageResource(R.drawable.user_icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView, 0); // Insert at the top
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
