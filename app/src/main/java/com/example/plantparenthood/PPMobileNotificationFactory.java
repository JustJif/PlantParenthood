package com.example.plantparenthood;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class PPMobileNotificationFactory extends AbstractPPNotificationFactory {

    private static final String CHANNEL_ID = "PlantParenthoodNotificationChannel";
    private static final int IMPORTANCE_DEFAULT = 0;

    /**
     * Creates the notification channel for the application.
     * Needs to be called as soon as the application starts.
     */
    public static void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }


    /**
     *
     * @param plantID The ID of the plant which needs to be watered.
     * @param plantName The name of the plant which needs to be watered.s
     * @param context
     * @return The NotificationCompat.Builder that has been created.
     */
    public Notification createWaterNotification(int plantID, String plantName, Context context){
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

        String title = plantName + " needs to be watered!";
        String content = "Your plant " + plantName + " needs to be watered!";

        notiBuilder.setSmallIcon(R.drawable.ic_notification_plant).setContentTitle(title).setContentText(content).setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return notiBuilder.build();
    }

}


