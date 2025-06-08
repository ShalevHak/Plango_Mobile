package com.example.calendarapp.Fragments.Content;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.calendarapp.Activities.CreateGroupActivity;
import com.example.calendarapp.Components.Groups.GroupsRecycler;
import com.example.calendarapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupsFragment extends Fragment {
    private FloatingActionButton fabGroupsAction;
    private GroupsRecycler groupsRecycler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        groupsRecycler = view.findViewById(R.id.groupsRecycler);
        fabGroupsAction = view.findViewById(R.id.fabGroupsAction);

        fabGroupsAction.setOnClickListener(v -> showGroupActionDialog());

        return view;
    }
    private void showGroupActionDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Group Options")
                .setItems(new CharSequence[]{"Create Group", "Join Group"}, (dialog, which) -> {
                    if (which == 0) {
                        // Start CreateGroupActivity
                        Intent intent = new Intent(requireContext(), CreateGroupActivity.class);
                        intent.putExtra("source_activity", "ContentActivity");
                        startActivity(intent);
                    } else if (which == 1) {
                        // Show placeholder dialog
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Coming Soon")
                                .setMessage("Join Group is under development.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                })
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(groupsRecycler != null){
            groupsRecycler.refresh();
        }
    }
}
