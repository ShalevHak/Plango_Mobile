package com.example.calendarapp.Fragments.Groups;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Activities.ContentActivity;
import com.example.calendarapp.Activities.CreateGroupActivity;
import com.example.calendarapp.Activities.GroupActivity;
import com.example.calendarapp.Managers.GroupsManager;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.ThemeUtils;

import java.util.HashMap;

public class GroupInfoFragment extends Fragment{

    private EditText editTextGroupName, editTextAbout;
    private TextView textViewVisibility;
    private LinearLayout layoutSelectedMembers;
    private Button btnLeave, btnEditGroup;
    private View layout;

    private static final String ARG_GROUP = "arg_group";   // <-- key for Bundle
    private Group group;
    private GroupsManager groupsManager = GroupsManager.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instead of reading from arguments, retrieve from Activity’s static variable:
        group = GroupActivity.getCurrentGroup();
        if (group == null) {
            Log.i("GroupInfoFragment", "setupUI called but currentGroup is null!");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_group_info, container, false);
        bindViews(layout);
        setupUI();

        return layout;
    }
    private void bindViews(View layout) {
        editTextGroupName   = layout.findViewById(R.id.editTextGroupName);
        editTextAbout       = layout.findViewById(R.id.editTextAbout);
        textViewVisibility  = layout.findViewById(R.id.textViewVisibility);
        layoutSelectedMembers = layout.findViewById(R.id.layoutSelectedMembers);
        btnLeave = layout.findViewById(R.id.btnLeaveGroup);
        btnEditGroup        = layout.findViewById(R.id.btnEditGroup);
    }
    private void setupUI() {
        if (group == null) {
            Log.i("GroupInfoFragment", "setupUI called but group is null!");
            return;
        }
        Log.i("GroupInfoFragment", "UI setup is called with group: " + group.toString());

        editTextGroupName.setText(group.getName());
        editTextAbout.setText(group.getAbout());
        textViewVisibility.setText(group.getVisibility());

        btnLeave.setText("Leave Group");
        btnLeave.setOnClickListener(v -> {
            createLeaveGroupDialog();
        });

        configureEditOptionForMembers();



        refreshMembersUI();
    }

    private void configureEditOptionForMembers() {
        String groupId = group.getId();
        groupsManager
                .isUserGroupsAdmin(groupId)
                .thenAccept(this::applyEditButtonState)
                .exceptionally(this::onAdminCheckFailed);
    }

    /**
     * Callback when we successfully determine whether the current user is an admin.
     */
    private void applyEditButtonState(boolean isAdmin) {
        if (isAdmin) {
            // Fully opaque and normal text/icon
            btnEditGroup.setAlpha(1f);
            btnEditGroup.setText("Edit Group");
            btnEditGroup.setClickable(true);
            btnEditGroup.setLongClickable(false);
            TooltipCompat.setTooltipText(btnEditGroup, null);

            setBtnEditGroupListener();
        } else {
            // Visually grey it out
            btnEditGroup.setAlpha(0.5f);
            // Optionally append a hint so users see why it's disabled
            btnEditGroup.setText("Edit Group (Admins Only)");
            btnEditGroup.setClickable(false);
            // Allow long-press so the tooltip shows
            btnEditGroup.setLongClickable(true);
            // Show a tooltip on long-press / hover
            TooltipCompat.setTooltipText(btnEditGroup, "Only admins can edit group settings");

            // Optionally: if you want to catch a tap and show a quick Toast instead of doing nothing:
            btnEditGroup.setOnClickListener(v ->
                    Toast.makeText(v.getContext(), "Only admins can edit group settings", Toast.LENGTH_SHORT).show()
            );
        }
    }

    /**
     * Error handler if the CompletableFuture chain fails.
     * Logs the exception, disables the button, and shows a generic tooltip.
     */
    private Void onAdminCheckFailed(Throwable throwable) {
        Log.e("GroupInfoFragment", "Failed to check admin status for group " + group.getId(), throwable);

        // Disable + grey-out + show generic tooltip
        btnEditGroup.setAlpha(0.5f);
        btnEditGroup.setClickable(false);
        btnEditGroup.setLongClickable(true);
        btnEditGroup.setText("Edit Group (Error)");
        TooltipCompat.setTooltipText(btnEditGroup, "Error determining permissions");

        btnEditGroup.setOnClickListener(v ->
                Toast.makeText(v.getContext(), "Error determining permissions", Toast.LENGTH_SHORT).show()
        );

        return null;
    }

    public void setBtnEditGroupListener() {
        btnEditGroup.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateGroupActivity.class);
            intent.putExtra("groupId", group.getId());
            intent.putExtra("source_activity", "GroupActivity");
            startActivity(intent);
        });
    }

    private void refreshMembersUI() {
        layoutSelectedMembers.removeAllViews();

        HashMap<String, String> members = new HashMap<>( group.getMembers());
        if (members == null || members.isEmpty()) {
            TextView noMembers = new TextView(getContext());
            noMembers.setText("No members");
            noMembers.setTextColor(getResources().getColor(android.R.color.darker_gray));
            noMembers.setPadding(8, 16, 8, 16);
            layoutSelectedMembers.addView(noMembers);
            return;
        }

        for (String email : members.keySet()) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(8, 12, 8, 12);

            TextView nameView = new TextView(getContext());
            nameView.setText(email);
            nameView.setTextColor(ThemeUtils.resolveColorFromTheme(getContext(),R.attr.colorPrimaryText));
            nameView.setTextSize(16);
            nameView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            row.addView(nameView);
            layoutSelectedMembers.addView(row);
        }
    }

    private void createLeaveGroupDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Leave Group")
                .setMessage("Are you sure you want to leave the group \"" + group.getName() + "\"?")
                .setPositiveButton("Leave", (dialog, which) -> {
                    groupsManager.leaveGroup(group)
                        .thenAccept(res -> {
                            Toast.makeText(getContext(), "You left the group", Toast.LENGTH_SHORT).show();
                            // Navigate back
                            goBackToContentActivity();
                        })
                        .exceptionally(
                             e -> {
                                 Log.e("GroupInfoFragment", "Failed leaving group: " + group.getName() + "\n" + e.toString());
                                 return null;
                             }
                        );
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void goBackToContentActivity() {
        Intent intent = new Intent(getContext(), ContentActivity.class);
        if(getContext() != null) getContext().startActivity(intent);
    }


}
