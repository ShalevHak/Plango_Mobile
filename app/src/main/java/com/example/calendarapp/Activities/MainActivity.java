package com.example.calendarapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.TokenManagement.TokenManager;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.NetworkUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initActivity();
    }



    private void initActivity() { // Initialize globally
        API.init(this);
        NetworkUtil.registerNetworkCallback(this);

        // Check if user is logged in (JWT exists)
        if (isLoggedIn()) {
            startActivity(new Intent(this, ContentActivity.class));
            finish(); // Close MainActivity so user can't go back to it
        }
        else {
            API.api().usersService.logout(null);
        }
    }

    private boolean isLoggedIn() {
        // TODO: fix - validate token and userID
        String token = TokenManager.getInstance().getToken();
        String userID = TokenManager.getInstance().getUserID();

        return userID != null && userID.isEmpty() && token != null && !token.isEmpty(); // User is logged in if token exists
    }
}