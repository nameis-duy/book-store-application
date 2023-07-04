package com.example.bookstoreappliaction.notification_config;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.constants.Constants;

public class NotificationConfig {

    public static Notification.Builder getBuilder(Context context, String title, String message) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.baseline_menu_book_24)
                .setColor(context.getColor(R.color.blue));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(Constants.NOTIFICATION_CHANNEL_ID);
        }

        return builder;
    }

    public static NotificationManager getNotificationManger(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, "Push Notification", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        return notificationManager;
    }
}
