package com.example.calendarapp.API.Interfaces;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Group {
    // Example properties
    private String name;
    private Bitmap profilePicture; // Stores the Bitmap directly

    // Constructor
    public Group(String name) {
        this.name = name;
        this.profilePicture = generateDefaultProfilePicture(name);
    }

    public String getName() {
        return name;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    // Method to generate the default profile picture
    private Bitmap generateDefaultProfilePicture(String name) {
        int width = 100;  // Width of the image
        int height = 100; // Height of the image

        // Create a Bitmap and Canvas to draw on
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw the background color (grey)
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        // Set up the paint for the text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Get the first two letters of the name
        String initials = name.length() >= 2 ? name.substring(0, 2).toUpperCase() : name.toUpperCase();

        // Calculate text position
        float x = width / 2f;
        float y = (height / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);

        // Draw the text
        canvas.drawText(initials, x, y, textPaint);

        return bitmap;
    }
}
