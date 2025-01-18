package com.example.calendarapp.API.Services;

import com.example.calendarapp.API.Interfaces.Group;
import com.example.calendarapp.API.Interfaces.User;

import java.util.Random;

public class GroupsService {
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

}
