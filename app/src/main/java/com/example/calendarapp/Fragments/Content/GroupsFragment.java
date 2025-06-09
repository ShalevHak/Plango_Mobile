package com.example.calendarapp.Fragments.Content;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Activities.CreateGroupActivity;
import com.example.calendarapp.Components.Groups.GroupsRecycler;
import com.example.calendarapp.Managers.GroupsManager;
import com.example.calendarapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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
                    }
                    else if (which == 1) {
                        // Show placeholder dialog
                        showJoinGroupDialog();
                    }
                })
                .show();
    }
    private void showJoinGroupDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_join_group, null);
        EditText input = dialogView.findViewById(R.id.editGroupNameSearch);
        ListView listView = dialogView.findViewById(R.id.listGroupsResults);
        TextView emptyView = dialogView.findViewById(R.id.textNoGroupsFound);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
        List<Group> foundGroups = new ArrayList<>();

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Join Group")
                .setView(dialogView)
                .setPositiveButton("Search", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            adapter.clear();
            String name = input.getText().toString().trim();
            if(name.isEmpty()) return;
            GroupsManager.getInstance().getGroupsByName(name)
                    .thenAccept(groups -> {
                        if(getActivity() != null) getActivity().runOnUiThread(() -> {
                            foundGroups.clear();
                            foundGroups.addAll(groups);
                            List<String> names = new ArrayList<>();
                            for(Group g : groups) names.add(g.getName());
                            adapter.addAll(names);
                            adapter.notifyDataSetChanged();
                            emptyView.setVisibility(groups.isEmpty() ? View.VISIBLE : View.GONE);
                        });
                    })
                    .exceptionally(e -> {
                        if(getActivity() != null) getActivity().runOnUiThread(() ->
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Error")
                                        .setMessage("Failed to search groups")
                                        .setPositiveButton("OK", null)
                                        .show()
                        );
                        return null;
                    });
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Group selected = foundGroups.get(position);
            GroupsManager.getInstance().joinGroup(selected)
                    .thenAccept(v -> {
                        if(getActivity() != null) getActivity().runOnUiThread(() -> {
                            groupsRecycler.refresh();
                            dialog.dismiss();
                            new AlertDialog.Builder(requireContext())
                                    .setMessage("Joined group!")
                                    .setPositiveButton("OK", null)
                                    .show();
                        });
                    })
                    .exceptionally(e -> {
                        if(getActivity() != null) getActivity().runOnUiThread(() ->
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Error")
                                        .setMessage("Failed to join group")
                                        .setPositiveButton("OK", null)
                                        .show()
                        );
                        return null;
                    });
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if(groupsRecycler != null){
            groupsRecycler.refresh();
        }
    }
}
