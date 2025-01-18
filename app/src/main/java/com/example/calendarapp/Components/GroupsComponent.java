package com.example.calendarapp.Components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.calendarapp.API.API;
import com.example.calendarapp.Components.Interfaces.IComponent;

public class GroupsComponent extends CustomContainerComponent implements IComponent {
    private API api;
    public GroupsComponent(Context context) {
        super(context);
        initComponent(context);
    }

    public GroupsComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public GroupsComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);

    }

    public GroupsComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        api = API.api();
//        Group[] groups = api.groupsService.getUsersGroups(api.usersService.user);
//
//        for(Group g : groups){
//            this.scrollerContent.addView(new GroupComponent(context, g));
//        }
    }
}
