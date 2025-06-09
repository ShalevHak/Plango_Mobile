package com.example.calendarapp.API;

import android.content.Context;
import android.util.Log;

import com.example.calendarapp.API.Services.ActivitiesService;
import com.example.calendarapp.API.Services.CalendarsService;
import com.example.calendarapp.API.Services.GroupsService;
import com.example.calendarapp.API.Services.UsersService;
import com.example.calendarapp.API.TokenManagement.TokenManager;

public class API {
    // *Local host config*
    //    private static final String PROTOCOL = "http";
//    private static final String API_IP = "172.21.112.1";
//    private static final String API_PORT = "8000";
//    private static final String BASE_ROUTE = "/api/v1-dev/";
//
//    public static String getRoute(String route) {
//        return PROTOCOL + "://" + API_IP + ":" + API_PORT + BASE_ROUTE + route + "/";
//    }

    // *Render Host Config*
    private static final String PROTOCOL = "https";
    private static final String API_IP = "plango-api-mobile-dev.onrender.com";
    // REMOVE PORT — HTTPS runs on default port 443
    private static final String BASE_ROUTE = "/api/v1-dev/";

    public static String getRoute(String route) {
        return PROTOCOL + "://" + API_IP + BASE_ROUTE + route + "/";
    }

    private static API api;
    private static Context context;
    // Service instances
    public final GroupsService groupsService;
    public final CalendarsService calendarsService;
    public final ActivitiesService activitiesService;
    public final UsersService usersService;
    // Private constructor
    private API(Context context) {
        TokenManager.init(context);
        this.calendarsService = new CalendarsService();
        this.activitiesService = new ActivitiesService();
        this.usersService = new UsersService();
        this.groupsService = new GroupsService();
    }
    private API() {
        this.calendarsService = new CalendarsService();
        this.activitiesService = new ActivitiesService();
        this.usersService = new UsersService();
        this.groupsService = new GroupsService();
    }

    // Public methods to provide the single instance
    public static void init(Context context){
        if(API.context == null) {
            API.context = context;
            Log.i("API","Init API");
            synchronized (API.class) { // Thread-safe
                api = new API(context);
            }
        }
    }
    public static API api(){
        if (api == null) {
            Log.i("API","Instantiating API");
            synchronized (API.class) { // Thread-safe
                if (api == null) {
                    api = new API();
                }
            }
        }
        return api;
    }
}
