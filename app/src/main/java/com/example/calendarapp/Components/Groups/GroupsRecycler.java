package com.example.calendarapp.Components.Groups;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.API.TokenManagement.TokenManager;
import com.example.calendarapp.Adapters.GroupsRecyclerAdapter;
import com.example.calendarapp.Components.Interfaces.IComponent;

public class GroupsRecycler extends RecyclerView implements IComponent {
    public GroupsRecycler(@NonNull Context context) {
        super(context);
        initComponent(context);
    }

    public GroupsRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public GroupsRecycler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    @Override
    public void initComponent(Context context) {
        this.setLayoutManager(new LinearLayoutManager(context));
        this.setAdapter(new GroupsRecyclerAdapter());
    }

    public void refresh(){
        if(getAdapter() != null && getAdapter() instanceof GroupsRecyclerAdapter){
            ((GroupsRecyclerAdapter) getAdapter()).refresh();
        }
    }
}
