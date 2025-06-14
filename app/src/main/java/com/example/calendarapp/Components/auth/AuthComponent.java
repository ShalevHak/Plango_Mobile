package com.example.calendarapp.Components.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Services.UsersService;
import com.example.calendarapp.Activities.ContentActivity;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.ErrorUtils;

public class AuthComponent extends LinearLayout implements IComponent {
    private boolean isSignIn = true; // Default to Sign-In

    private View signInView, signUpView,  toggleAutoLink;

    private Button btnAuth;
    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private CheckBox cbShowPassword;
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
        etConfirmPassword = new EditText(getContext()); // To ensure not null
        btnAuth = view.findViewById(R.id.btnSignIn);
        toggleAutoLink = view.findViewById(R.id.llLinkToSignUp);
        cbShowPassword = view.findViewById(R.id.cbShowPassword);
        initListeners();
    }
    private void initSignUpViews(View view) {
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etSignUpEmail);
        etPassword = view.findViewById(R.id.etSignUpPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnAuth = view.findViewById(R.id.btnSignUp);
        toggleAutoLink = view.findViewById(R.id.llLinkToSignIn);
        cbShowPassword = view.findViewById(R.id.cbShowPasswords);
        initListeners();
    }

    private void initListeners() {
        toggleAutoLink.setOnClickListener(v -> openLink());
        btnAuth.setOnClickListener(v -> handleAuth());
        cbShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> showPassword(isChecked));
    }

    private void showPassword(boolean isChecked) {
        etPassword.setTransformationMethod(
                !isChecked
                    ? new PasswordTransformationMethod()
                    : null);
        etConfirmPassword.setTransformationMethod(
                !isChecked
                    ? new PasswordTransformationMethod()
                    : null);
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
    }

    private void handleSignIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        setClickableButtons(false);
        // Add your sign-in logic here
        this.api.usersService.login(email,password).thenAccept(authResponse -> {
                Log.i("LOGIN", "Login successful!");
                navigateToContentActivity();
                setClickableButtons(true);
            })
            .exceptionally(e -> {
                Log.e("LOGIN", "Login failed: " + e.getMessage());
                setClickableButtons(true);
                new AlertDialog.Builder(getContext())
                        .setTitle("Log In Error")
                        .setMessage(ErrorUtils.getCause(e))
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
                return null;
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
        this.api.usersService.signup(name, email, password, confirmPassword).thenAccept(
                authResponse -> {
                    Log.i("SIGNUP", "Sign up successful!");
                    navigateToContentActivity();
                    setClickableButtons(true);
                })
                .exceptionally(e ->
                {
                    setClickableButtons(true);
                    new AlertDialog.Builder(getContext())
                            .setTitle("Sign Up Error")
                            .setMessage(ErrorUtils.getCause(e))
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();
                    return null;
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

