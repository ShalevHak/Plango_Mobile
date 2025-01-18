package com.example.calendarapp.Components.auth;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.API;
import com.example.calendarapp.Activities.ContentActivity;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

public class GoogleAuthComponent extends LinearLayout implements IComponent {
    private boolean isSignIn = true;
    private Button btnGoogleAuth;
    private API api;

    public GoogleAuthComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public GoogleAuthComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public GoogleAuthComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public GoogleAuthComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        this.api = API.api();
        LayoutInflater.from(context).inflate(R.layout.google_auth_component,this);
        btnGoogleAuth = findViewById(R.id.btnGoogleAuth);
        btnGoogleAuth.setOnClickListener(v -> handleGoogleAuth());
    }

    private void handleGoogleAuth() {
        //this.api.usersService.Login("","");
        navigateToContentActivity();
    }
    private void navigateToContentActivity() {
        Intent intent = new Intent(this.getContext(), ContentActivity.class);
        getContext().startActivity(intent);
    }
    public void setIsSignIn(boolean isSignIn){
        this.isSignIn = isSignIn;
        if(isSignIn){
            btnGoogleAuth.setText("SIGN IN WITH GOOGLE");
        }
        else {
            btnGoogleAuth.setText("SIGN UP WITH GOOGLE");
        }
    }
    public void setClickableGoogleAuthBtn(boolean isClickable){
        btnGoogleAuth.setClickable(isClickable);
    }

}
