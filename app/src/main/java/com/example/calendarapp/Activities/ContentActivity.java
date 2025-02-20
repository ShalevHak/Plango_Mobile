package com.example.calendarapp.Activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.calendarapp.Components.ToolbarComponent;
import com.example.calendarapp.Fragments.UserFragment;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.NetworkUtil;

public class ContentActivity extends AppCompatActivity {
    private ToolbarComponent toolbar;
    private FrameLayout flFragmentContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);
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
        this.flFragmentContainer = findViewById(R.id.flFragmentContainer);
        fragmentManager.beginTransaction()
                .replace(flFragmentContainer.getId(), new UserFragment())
                .addToBackStack(null) // Optional: Adds the transaction to the back stack
                .commit();
        this.toolbar = findViewById(R.id.tbMain);
        this.toolbar.setFragmentContainer(flFragmentContainer);
        this.toolbar.setFragmentManager(fragmentManager);

        // Disables the back button by setting the callback to 'false' (disabled).
        // This prevents the back button from performing any action.
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }
}