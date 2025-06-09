package com.example.calendarapp.API.FetchingHelpers;

import com.example.calendarapp.API.Interfaces.Event;
import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.API.Responses.DefaultResponse;
import com.example.calendarapp.API.Responses.HandlerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    Call<HandlerResponse<Group>> updateGroup(@Path("groupId") String originalGroupId, @Body Group group);
    @GET("{groupId}")
    Call<HandlerResponse<Group>> getGroupById(@Path("groupId") String groupId);

    @GET("search")
    Call<HandlerResponse<List<Group>>> searchGroupByName(@Query("name") String name);
}
