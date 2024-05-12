package com.cseradam.szkt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String stopName = sharedPreferences.getString("stopName", "Center");


        // Create a notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "reminder_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Reminder Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, channelId)
                    .setContentTitle("Megálló emlékeztető")
                    .setContentText("El kell indulnod a(z) " + stopName + " megállóhoz. 5 perc mulva indul a járatod.")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        }

        // Send the notification
        notificationManager.notify(0, notification);
    }
}
