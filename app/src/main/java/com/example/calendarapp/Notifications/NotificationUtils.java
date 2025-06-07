package com.example.calendarapp.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.calendarapp.API.Interfaces.Event;

public class NotificationUtils {
    public static void scheduleEventReminder(Context context, Event event) {
        scheduleEventReminder(context, event, 0);
    }

    public static void scheduleEventReminder(Context context, Event event, long offsetMillis) {
        long triggerAt = event.getStartDate().getTime() - offsetMillis;
        if (triggerAt < System.currentTimeMillis()) return;

        Intent intent = new Intent(context, EventReminderReceiver.class);
        intent.putExtra("title", event.getTitle());

        int requestCode = (int) triggerAt;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent);
        }
    }
}
