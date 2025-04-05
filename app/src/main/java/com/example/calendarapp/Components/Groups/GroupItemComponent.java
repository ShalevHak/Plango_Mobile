package com.example.calendarapp.Components.Groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Activities.GroupActivity;
import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.R;

public class GroupItemComponent extends LinearLayout implements IComponent, View.OnClickListener {
    private Group group;

    public GroupItemComponent(Context context) {
        super(context);
        initComponent(context);
    }
    public GroupItemComponent(Context context, Group group) {
        super(context);
        this.group = group;
        initComponent(context);
    }

    public GroupItemComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public GroupItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public GroupItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        LayoutInflater.from(context).inflate(R.layout.group_component, this, true);
        ImageView groupIcon = findViewById(R.id.ivGroupIcon);
        TextView groupName = findViewById(R.id.tvGroupName);
        this.setClickable(true);
        this.setOnClickListener(this);

        if(group==null){
            this.group = new Group("");
        }
        Bitmap groupBitmap = group.getProfilePicture(); // Assuming Group has getProfilePicture()
        if (groupBitmap != null) {
            groupIcon.setImageBitmap(groupBitmap);
        } else {
            groupIcon.setImageResource(R.drawable.user_icon); // Fallback icon
        }

        // Set the name of the group
        groupName.setText(group.getName());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getContext(), GroupActivity.class);
        this.getContext().startActivity(intent);
    }

    public void setGroup(Group group) {
        this.group = group;
        initComponent(getContext()); // Re-bind the layout
    }
}
