package com.example.calendarapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Components.Toolbars.GroupToolbarComponent;
import com.example.calendarapp.Fragments.Groups.GroupInfoFragment;
import com.example.calendarapp.Managers.GroupsManager;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.NetworkUtil;

public class GroupActivity extends AppCompatActivity {

    private FrameLayout flFragmentContainerGroup;
    private FragmentManager fragmentManager;
    private GroupToolbarComponent toolbar;

    private static Group currentGroup;

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

        String groupId = getIntent().getStringExtra("groupId");
        NetworkUtil.registerNetworkCallback(this);
        this.fragmentManager = getSupportFragmentManager();
        this.flFragmentContainerGroup = findViewById(R.id.flFragmentContainerGroup);
        this.toolbar = findViewById(R.id.tbGroup);


        if (groupId != null) {
            fetchGroup(groupId); // or from local cache
        }
        else{
            Toast.makeText(this,"No group to show",Toast.LENGTH_LONG).show();
        }
    }

    private void fetchGroup(String groupId) {
        GroupsManager.getInstance().getGroupById(groupId)
                .thenAccept(group ->{
                    currentGroup = group;

                    this.toolbar.initFragmentManagement(flFragmentContainerGroup,fragmentManager);
                })
                .exceptionally(e ->{
                    Log.e("GroupActivity","Unable to load group: " + groupId + "\n" + e.toString());
                    return null;
                });
    }

    public static Group getCurrentGroup() {
        return currentGroup;
    }
}