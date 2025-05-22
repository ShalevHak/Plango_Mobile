package com.example.calendarapp.API.Services;

import android.util.Log;

import com.example.calendarapp.API.API;
import com.example.calendarapp.API.FetchingHelpers.GroupHelper;
import com.example.calendarapp.API.Interceptors.AuthInterceptor;
import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.API.Interfaces.User;
import com.example.calendarapp.API.Responses.DefaultResponse;
import com.example.calendarapp.API.Responses.EventsResponse;
import com.example.calendarapp.API.Responses.GetGroupsResponse;
import com.example.calendarapp.API.Responses.HandlerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupsService extends AbstractAPIService<GroupHelper>{
    private String GROUP_URL;
    public GroupsService() {
        super(GroupHelper.class);
    }
    public Group[] getUsersGroups(User user) {
        // Business places
        String[] businessTypes = {
                "Cafe", "Shop", "Market", "Bank", "Hotel", "Restaurant", "Lounge",
                "Library", "Bakery", "Studio", "Gallery", "Gym", "Boutique", "Pharmacy", "Warehouse"
        };


        Group[] groups = new Group[30]; // Create an array to hold 30 groups
        Random random = new Random();

        for (int i = 0; i < groups.length; i++) {
            // Randomly select a prefix and a business type
            String businessType = businessTypes[random.nextInt(businessTypes.length)];

            // Combine them to create a unique group name
            String groupName = businessType + " #"+i;

            // Create and add the group to the array
            groups[i] = new Group(groupName);
        }

        return groups;
    }

    @Override
    protected void initRetrofit() {
        GROUP_URL = API.getRoute("groups");

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .enableComplexMapKeySerialization() // for maps parsing
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(GROUP_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)) // using corrected gson
                .build();
    }

    public CompletableFuture<HandlerResponse<Group>> updateGroup(String originalGroupId, Group newGroup) {
        CompletableFuture<HandlerResponse<Group>> future = new CompletableFuture<>();
        Call<HandlerResponse<Group>> call = fetchingHelper.updateGroup(originalGroupId,newGroup);

        call.enqueue(new Callback<HandlerResponse<Group>>() {
            @Override
            public void onResponse(Call<HandlerResponse<Group>> call, Response<HandlerResponse<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("GroupsService","Group was updated successfully" + response.body().toString());
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<HandlerResponse<Group>> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;

    }

    public CompletableFuture<Void> addGroup(Group newGroup) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Call<DefaultResponse> call = fetchingHelper.createGroup("",newGroup);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("GroupsService","Group was created successfully");
                    future.complete(null);
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public  CompletableFuture<HandlerResponse<Group>> getGroupById(String groupId) {
        CompletableFuture<HandlerResponse<Group>> future = new CompletableFuture<>();
        Call<HandlerResponse<Group>> call = fetchingHelper.getGroupById(groupId);

        call.enqueue(new Callback<HandlerResponse<Group>>() {
            @Override
            public void onResponse(Call<HandlerResponse<Group>> call, Response<HandlerResponse<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<HandlerResponse<Group>> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }
}
