package com.example.calendarapp.Managers;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.API.Responses.GetGroupsResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GroupsManager {
    private static GroupsManager groupsManager;
    private API api;
    private GroupsManager(){
        api = API.api();
    }

    public static GroupsManager getInstance(){
        if (groupsManager == null) {
            Log.i("CalendarService","Init CalendarService");
            synchronized (GroupsManager.class) { // Thread-safe
                if (groupsManager == null) {
                    groupsManager = new GroupsManager();
                }
            }
        }
        return groupsManager;
    }

    public CompletableFuture<List<Group>> loadGroupsForUser(String id){
        CompletableFuture<List<Group>> future = new CompletableFuture<>();
        api.usersService.getGroupsById(id).thenAccept(res -> {
            future.complete(res.groups);
        })
        .exceptionally(e -> {
            Log.e("GroupsManager","failed to load groups from id: " + id);
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<Void> createGroup(Group newGroup) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        API.api().groupsService.addGroup(newGroup)
                .thenAccept(result -> {
                    future.complete(null);  // success
                })
                .exceptionally(e-> {
                    future.completeExceptionally(e);  // fail
                    return null;
                });

        return future;
    }

    public CompletableFuture<Void> updateGroup(String originalGroupId, Group newGroup) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        API.api().groupsService.updateGroup(originalGroupId, newGroup)
                .thenAccept(result -> {
                    future.complete(null);  // success
                })
                .exceptionally(e-> {
                    future.completeExceptionally(e);  // fail
                    return null;
                });

        return future;
    }


}
