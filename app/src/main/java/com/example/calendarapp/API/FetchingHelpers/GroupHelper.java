package com.example.calendarapp.API.FetchingHelpers;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.API.Responses.DefaultResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GroupHelper {

    @POST()
    Call<DefaultResponse> createGroup(
            @Url String url,
            @Body Group group
    );

    @PATCH("{groupId}")
    Call<DefaultResponse> updateGroup(@Path("originalGroupId") String originalGroupId, Group newGroup);
}
