package com.example.calendarapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.calendarapp.Components.ToolbarComponent;
import com.example.calendarapp.Fragments.UserFragment;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.NetworkUtil;

public class GroupActivity extends AppCompatActivity {

    private FrameLayout flFragmentContainerGroup;
    private ToolbarComponent toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initActivity();
    }

    private void initActivity() {
        NetworkUtil.registerNetworkCallback(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        this.flFragmentContainerGroup = findViewById(R.id.flFragmentContainerGroup);
        fragmentManager.beginTransaction()
                .replace(flFragmentContainerGroup.getId(), new UserFragment())
                .addToBackStack(null) // Optional: Adds the transaction to the back stack
                .commit();
        this.toolbar = findViewById(R.id.tbMain);
        toolbar.setFragmentContainer(flFragmentContainerGroup);
        toolbar.setFragmentManager(fragmentManager);
    }
}