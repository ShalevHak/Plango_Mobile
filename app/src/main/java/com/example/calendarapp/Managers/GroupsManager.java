package com.example.calendarapp.Managers;

import android.os.strictmode.Violation;
import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.Interfaces.Group;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

    public CompletableFuture<Group> updateGroup(String originalGroupId, Group newGroup) {
        CompletableFuture<Group> future = new CompletableFuture<>();
        Log.i("GroupsManager","Updating Group: " + newGroup);
        API.api().groupsService.updateGroup(originalGroupId, newGroup)
                .thenAccept(res -> {
                    future.complete(res.data);  // success
                })
                .exceptionally(e-> {
                    future.completeExceptionally(e);  // fail
                    return null;
                });

        return future;
    }


    public CompletableFuture<Group> getGroupById(String groupId) {
        CompletableFuture<Group> future = new CompletableFuture<>();
        API.api().groupsService.getGroupById(groupId)
                .thenAccept(groupHandlerResponse -> {
                    if(!groupHandlerResponse.status.equals("success")||groupHandlerResponse.data == null) future.completeExceptionally(new Exception("Fetched a null group"));
                    Log.i("GroupsManager","Successfully fetched group: " + groupId);
                    future.complete(groupHandlerResponse.data);
                })
                .exceptionally(
                        e -> {
                            future.completeExceptionally(e);
                            return null;
                        }
                );

        return future;
    }

    public CompletableFuture<Void> leaveGroup(Group group) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        API.api().usersService.getMe().thenAccept(userResponse -> {
            group.removeMember(userResponse.user.getEmail());
            Group updatedGroup = new Group(group);
            updateGroup(group.getId(),updatedGroup).thenAccept(res -> future.complete(null))
                    .exceptionally(e -> {
                       future.completeExceptionally(e);
                       return null;
                    });
        }).exceptionally(e -> {
            future.completeExceptionally(e);
            return null;
        });
        return future;
    }

    public CompletableFuture<Boolean> isUserGroupsAdmin(String groupId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        API.api().usersService.getMe().thenAccept(
                userResponse ->
                {
                    String email = userResponse.user.getEmail();
                    API.api().groupsService.getGroupById(groupId).thenAccept(
                            groupHandlerResponse ->
                            {
                                HashMap<String, String> members = groupHandlerResponse.data.getMembers();
                                String userRole = members.getOrDefault(email, "");
                                future.complete(Objects.equals(userRole, Group.OWNER) || Objects.equals(userRole, Group.ADMIN));
                            }
                    )
                    .exceptionally(e -> {
                        future.completeExceptionally(e);
                        return null;
                    });
                })
                .exceptionally(
                        e -> {
                            future.completeExceptionally(e);
                            return null;
                        }
                );
        return future;
    }
}
