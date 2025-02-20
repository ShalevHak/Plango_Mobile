package com.example.calendarapp.Components.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Services.UsersService;
import com.example.calendarapp.Activities.ContentActivity;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

public class AuthComponent extends LinearLayout implements IComponent {
    private boolean isSignIn = true; // Default to Sign-In

    private View signInView, signUpView,  toggleAutoLink;

    private Button btnAuth;
    private GoogleAuthComponent googleAutoComponent;
    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private API api;
    public AuthComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public AuthComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public AuthComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        this.api = API.api();
        // Inflate sign-in and sign-up layouts into the parent
        LayoutInflater inflater = LayoutInflater.from(context);

        signInView = inflater.inflate(R.layout.signin_component, this, false);
        signUpView = inflater.inflate(R.layout.signup_component, this, false);

        addView(signInView); // Add Sign-In layout by default
        addView(signUpView); // Add Sign-Up layout (hidden by default)


        initViews();

        // Show the default layout
        toggleMode(isSignIn);
    }

    private void initSignInViews(View view) {
        etEmail = view.findViewById(R.id.etSignInEmail);
        etPassword = view.findViewById(R.id.etSignInPassword);
        btnAuth = view.findViewById(R.id.btnSignIn);
        googleAutoComponent = view.findViewById(R.id.GoogleAutoSignIn);
        toggleAutoLink = view.findViewById(R.id.flLinkToSignUp);
        initListeners();
    }



    private void initSignUpViews(View view) {
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etSignUpEmail);
        etPassword = view.findViewById(R.id.etSignUpPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnAuth = view.findViewById(R.id.btnSignUp);
        googleAutoComponent = view.findViewById(R.id.GoogleAutoSignUp);
        toggleAutoLink = view.findViewById(R.id.flLinkToSignIn);
        initListeners();
    }

    private void initListeners() {
        toggleAutoLink.setOnClickListener(v -> openLink());
        btnAuth.setOnClickListener(v -> handleAuth());
    }

    private void handleAuth() {
        boolean authenticationSucceeded;
        if(isSignIn){
            handleSignIn();
        }
        else {
            handleSignUp();
        }
    }

    private void navigateToContentActivity() {
        Intent intent = new Intent(this.getContext(), ContentActivity.class);
        getContext().startActivity(intent);
    }

    private void setClickableButtons(boolean isClickable){
        btnAuth.setClickable(isClickable);
        toggleAutoLink.setClickable(isClickable);
        googleAutoComponent.setClickableGoogleAuthBtn(isClickable);
    }
    private void handleSignIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        setClickableButtons(false);
        // Add your sign-in logic here
        this.api.usersService.login(email,password,new UsersService.AuthCallback() {
            @Override
            public void onSuccess() {
                Log.i("LOGIN", "Login successful!");
                // Navigate to the next screen
                navigateToContentActivity();
                setClickableButtons(true);

            }

            @Override
            public void onError(String errorMessage) {
                Log.e("LOGIN", "Login failed: " + errorMessage);
                setClickableButtons(true);
                // Show error message to the user
                new AlertDialog.Builder(getContext())
                        .setTitle("Log In Error")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private void handleSignUp() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        // Add your sign-up logic here
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            // Show a dialog to notify the user
            new AlertDialog.Builder(getContext())
                    .setTitle("Error")
                    .setMessage("Passwords do not match. Please try again.")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
            return;
        }
        setClickableButtons(false);
        this.api.usersService.signup(name, email, password, confirmPassword, new UsersService.AuthCallback() {
            @Override
            public void onSuccess() {
                Log.i("SIGNUP", "Sign up successful!");
                navigateToContentActivity();
                setClickableButtons(true);
            }

            @Override
            public void onError(String errorMessage) {
                setClickableButtons(true);
                new AlertDialog.Builder(getContext())
                        .setTitle("Sign Up Error")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    public void toggleMode(boolean signIn) {
        this.isSignIn = signIn;

        // Toggle visibility of the two views
        signInView.setVisibility(isSignIn ? VISIBLE : GONE);
        signUpView.setVisibility(isSignIn ? GONE : VISIBLE);
    }
    private void openLink() {
        isSignIn = !isSignIn;
        googleAutoComponent.setIsSignIn(isSignIn);
        toggleMode(isSignIn);
        initViews();
    }

    private void initViews() {
        // Initialize views in each layout
        if(isSignIn){
            initSignInViews(signInView);
        }
        else{
            initSignUpViews(signUpView);
        }
    }

}

