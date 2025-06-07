package com.example.calendarapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Managers.GroupsManager;
import com.example.calendarapp.Managers.UsersManager;
import com.example.calendarapp.R;
import com.example.calendarapp.Utils.ErrorUtils;
import com.example.calendarapp.Utils.NetworkUtil;
import com.example.calendarapp.Utils.ThemeUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText editTextGroupName, editTextAbout;
    private Spinner spinnerVisibility;
    private Button btnSaveGroup;
    private Spinner spinnerGroupColor;
    private View viewColorPreview;
    private int selectedColorAttr;
    private String selectedColorName;

    private Button btnSelectMembers;
    private Button btnCancel;
    private LinearLayout layoutSelectedMembers;

    private HashMap<String, String> selectedMembers = new HashMap<>();

    private static final int[] eventColors = ThemeUtils.eventColorsID;
    private static final String[] colorNames = ThemeUtils.eventColorsNames;

    private String sourceActivity;
    private String originalGroupId;

    private Group currentGroup = new Group("");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initActivity();
        
    }

    private void initActivity() {
        NetworkUtil.registerNetworkCallback(this);

        sourceActivity = getIntent().getStringExtra("source_activity");
        originalGroupId = getIntent().getStringExtra("groupId");

        // Initialize views
        editTextGroupName = findViewById(R.id.editTextGroupName);
        editTextAbout = findViewById(R.id.editTextAbout);
        btnSaveGroup = findViewById(R.id.btnSaveGroup);

        // Set up color spinner
        spinnerGroupColor = findViewById(R.id.spinnerGroupColor);
        viewColorPreview = findViewById(R.id.viewColorPreview);
        setupColorPicker(this);

        // Set up visibility spinner
        spinnerVisibility = findViewById(R.id.spinnerVisibility);

        ArrayAdapter<String> visibilityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Private","Public"}
        );
        spinnerVisibility.setAdapter(visibilityAdapter);


        // Save action
        btnSaveGroup.setOnClickListener(v -> saveGroup());

        // Members List

        btnSelectMembers = findViewById(R.id.btnSelectMembers);
        layoutSelectedMembers = findViewById(R.id.layoutSelectedMembers);

        // If editing a group with existing members
        if (selectedMembers != null && !selectedMembers.isEmpty()) {
            updateMembersUI();
        }


        btnSelectMembers.setOnClickListener(v -> showSelectMembersDialog());

        // Cancel Button
        btnCancel = findViewById(R.id.btnCancelGroup);
        btnCancel.setOnClickListener(v -> goBackToSourceActivity());

        // Display values from group
        if(originalGroupId != null && !originalGroupId.isEmpty()){
            updateFormFields();
        }
    }

    private void updateFormFields() {
        GroupsManager.getInstance().getGroupById(originalGroupId)
                .thenAccept(group ->{
                    this.currentGroup = group;

                    // Set group name & about
                    editTextGroupName.setText(group.getName());
                    editTextAbout.setText(group.getAbout());

                    // Set color spinner selection
                    selectedColorName = group.getColor();

                    List<String> colorNamesList = Arrays.asList(ThemeUtils.eventColorsNames);
                    int position = colorNamesList.indexOf(selectedColorName);

                    if (position == -1) position = 0;

                    spinnerGroupColor.setSelection(position); // This will trigger onItemSelected


                    // Set visibility spinner
                    if (group.getVisibility().equalsIgnoreCase("public")) {
                        spinnerVisibility.setSelection(1); // Public
                    } else {
                        spinnerVisibility.setSelection(0); // Private
                    }

                    // Set members
                    selectedMembers = new HashMap<>(group.getMembers());
                    updateMembersUI();
                })
                .exceptionally(e ->{
                    Log.e("CreateGroupActivity","Unable to load group: " + originalGroupId + "\n" + e.toString());
                    return null;
                });
    }

    private void setupColorPicker(Context context) {
        selectedColorAttr = eventColors[0];
        selectedColorName = colorNames[0];

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, colorNames);
        spinnerGroupColor.setAdapter(adapter);

        spinnerGroupColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColorName = colorNames[position];
                selectedColorAttr = ThemeUtils.getColorIDFromName(selectedColorName);
                int resolvedColor = ThemeUtils.resolveColorFromTheme(context, selectedColorAttr);
                viewColorPreview.setBackgroundColor(resolvedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerGroupColor.setSelection(0);
            }
        });
    }
    private void saveGroup() {
        // Get and trim values
        String name = editTextGroupName.getText().toString().trim();
        String about = editTextAbout.getText().toString().trim();
        String visibility = spinnerVisibility.getSelectedItem().toString();

        // Validate required fields
        if (name.isEmpty()) {
            editTextGroupName.setError("Group name is required");
            editTextGroupName.requestFocus();
            return;
        }

        currentGroup = new Group(name,about,selectedColorName, visibility, selectedMembers);
        Log.i("CreateGroupActivity","Saving Group: " + currentGroup);
        if(currentGroup == null) {
            Log.e("CreateGroupActivity","Error!");
        }
        if (originalGroupId != null && !originalGroupId.isEmpty()) {
            GroupsManager.getInstance().updateGroup(originalGroupId, currentGroup)
                    .thenAccept( group -> {
                        currentGroup = group;
                        if(group == null){
                            Toast.makeText(this,"Updated group was null. Going back to Content Activity",Toast.LENGTH_LONG).show();
                            sourceActivity = "ContentActivity";
                        }
                        goBackToSourceActivity();
                    })
                    .exceptionally(e -> {
                        Log.e("CreateGroupActivity", "Could not update group: " + e.getMessage() + "\n" + e.toString());
                        return null;
                    });
        } else {
            GroupsManager.getInstance().createGroup(currentGroup)
                    .thenRun(this::goBackToSourceActivity)
                    .exceptionally(e -> {
                        Toast.makeText(this,"Could not create group",Toast.LENGTH_LONG).show();
                        Log.e("CreateGroupActivity", "Could not create group: " + ErrorUtils.getCause(e));
                        return null;
                    });
        }
    }
    private void goBackToSourceActivity() {
        Intent returnIntent;
        if (sourceActivity == null) {
            sourceActivity = "";
        }
        switch (sourceActivity){
            case "ContentActivity":
                Log.i("CreateGroupActivity","Sending back to ContentActivity");
                returnIntent = new Intent(this, ContentActivity.class);
                returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(returnIntent);
                finish();
                break;
            case "GroupActivity":
                returnIntent = new Intent(this, GroupActivity.class);
                String groupId = currentGroup.getId();
                returnIntent.putExtra("groupId",groupId);
                startActivity(returnIntent);
                finish();
                break;
            default:
                // Optionally close the activity
                finish();
                break;
        }
    }

    private void showSelectMembersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_select_members, null);

        EditText editTextSearchEmail = view.findViewById(R.id.etSearchEmail);
        TextView textFoundUser = view.findViewById(R.id.textFoundUser);
        Button buttonAddFoundUser = view.findViewById(R.id.buttonAddFoundUser);
        LinearLayout layoutCurrentMembers = view.findViewById(R.id.layoutCurrentMembers);
        Button btnSearchEmail = view.findViewById(R.id.btnSearchEmail);

        // Create a local copy of the members list
        HashMap<String, String> tempSelectedMembers = new HashMap<>(selectedMembers);

        // Refresh dialog UI with temp members
        refreshEditMembersDialogUI(layoutCurrentMembers, tempSelectedMembers);

        // Search listener
        btnSearchEmail.setOnClickListener(btn -> {
            String email = editTextSearchEmail.getText().toString().trim();
            if (email.isEmpty()) {
                textFoundUser.setText("User info will show here");
                buttonAddFoundUser.setVisibility(View.GONE);
                return;
            }

            UsersManager.getInstance().searchUserByEmail(email)
                    .thenAccept(user -> runOnUiThread(() -> {
                        textFoundUser.setText(user.getName() + " (" + user.getEmail() + ")");
                        buttonAddFoundUser.setVisibility(View.VISIBLE);

                        buttonAddFoundUser.setOnClickListener(v -> {
                            tempSelectedMembers.put(user.getEmail(), "member");
                            refreshEditMembersDialogUI(layoutCurrentMembers, tempSelectedMembers);
                            Toast.makeText(this, "User added as member", Toast.LENGTH_SHORT).show();
                        });
                    }))
                    .exceptionally(e -> {
                        runOnUiThread(() -> {
                            textFoundUser.setText("User not found");
                            buttonAddFoundUser.setVisibility(View.GONE);
                        });
                        return null;
                    });
        });

        builder.setView(view);

        // Cancel = discard changes
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Save = apply temp changes to real selectedMembers
        builder.setPositiveButton("Save", (dialog, which) -> {
            selectedMembers.clear();
            selectedMembers.putAll(tempSelectedMembers);
            updateMembersUI();
        });

        builder.show();
    }


    private void refreshEditMembersDialogUI(LinearLayout layout, HashMap<String, String> members) {
        layout.removeAllViews();
        for (String email : members.keySet()) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 8, 0, 8);

            TextView emailView = new TextView(this);
            emailView.setText(email);
            emailView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            Button removeBtn = new Button(this);
            removeBtn.setText("Remove");
            removeBtn.setOnClickListener(v -> {
                members.remove(email);
                refreshEditMembersDialogUI(layout, members);
            });

            row.addView(emailView);
            row.addView(removeBtn);
            layout.addView(row);
        }
    }


    private void updateMembersUI() {
        layoutSelectedMembers.removeAllViews();

        if (selectedMembers.isEmpty()) {
            TextView noMembers = new TextView(this);
            noMembers.setText("No members selected");
            noMembers.setTextColor(getResources().getColor(android.R.color.darker_gray));
            layoutSelectedMembers.addView(noMembers);
            return;
        }

        for (String email : selectedMembers.keySet()) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 8, 0, 8);

            TextView emailView = new TextView(this);
            emailView.setText(email);
            emailView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            row.addView(emailView);
            layoutSelectedMembers.addView(row);
        }
    }

}