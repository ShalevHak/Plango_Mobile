package com.example.calendarapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.Components.Calendars.DayComponent;
import com.example.calendarapp.Components.Groups.GroupItemComponent;
import com.example.calendarapp.Managers.GroupsManager;
import com.example.calendarapp.R;

import java.util.ArrayList;
import java.util.List;

public class GroupsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_GROUP = 1;
    private List<Group> groups = new ArrayList<>();
    public GroupsRecyclerAdapter(String userId){
        GroupsManager.getInstance().loadGroupsForUser(userId).thenAccept(loadedGroups -> {
            Log.i("GroupsAdapter", "Loaded groups: " + loadedGroups.size());
            groups.clear();
            groups.addAll(loadedGroups);
            notifyDataSetChanged(); // trigger RecyclerView update
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_empty_component, parent, false);
            return new EmptyViewHolder(view);
        }

        GroupItemComponent item = new GroupItemComponent(parent.getContext());
        item.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new GroupViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_GROUP && holder instanceof GroupViewHolder) {
            ((GroupViewHolder) holder).bind(groups.get(position));
        }
        // No binding needed for empty view
    }

    @Override
    public int getItemViewType(int position) {
        return groups.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_GROUP;
    }

    @Override
    public int getItemCount() {
        return Math.max(groups.size(),1);
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Group group) {
            ((GroupItemComponent) itemView).setGroup(group);
            Log.i("GroupsRecyclerAdapter","binding view");
        }
    }
    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
