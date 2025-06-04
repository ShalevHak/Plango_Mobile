package com.example.calendarapp.Components.Groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
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
import com.example.calendarapp.Utils.ThemeUtils;

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
        LayoutInflater.from(context).inflate(R.layout.group_item_component, this, true);
        ImageView groupIcon = findViewById(R.id.ivGroupIcon);
        TextView groupName = findViewById(R.id.tvGroupName);
        View colorStripe = findViewById(R.id.viewColorStripe);

        this.setClickable(true);
        this.setOnClickListener(this);

        if (group == null) {
            this.group = new Group(""); // fallback
        }


        groupIcon.setImageResource(R.drawable.user_icon);


        groupName.setText(group.getName());


        groupName.setTextColor(ThemeUtils.resolveColorFromTheme(getContext(),R.attr.colorPrimaryText));

        // 🎨 Apply group color
        if (group.getColor() != null && !group.getColor().isEmpty()) {
            int colorId = ThemeUtils.getColorIDFromName(group.getColor());
            int color = ThemeUtils.resolveColorFromTheme(getContext(), colorId);
            colorStripe.setBackgroundColor(color);
        } else {
            colorStripe.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this.getContext(), GroupActivity.class);
        intent.putExtra("groupId", group.getId());
        intent.putExtra("source_activity","ContentActivity");
        Log.i("GroupItemComponent","Clicked group: \n" + group.toString());
        this.getContext().startActivity(intent);
    }

    public void setGroup(Group group) {
        this.group = group;
        initComponent(getContext()); // Re-bind the layout
    }
}
