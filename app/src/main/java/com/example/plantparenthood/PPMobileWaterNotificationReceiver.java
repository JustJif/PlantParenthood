/**
 * A receiver class is required in order to schedule a notification because of the way android works.
 * This is not a part of our class diagram for that reason, we did not know we would need this until now.
 */

package com.example.plantparenthood;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PPMobileWaterNotificationReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle the notification here
            int plantID = intent.getIntExtra("plantID", -1);
            String plantName = intent.getStringExtra("plantName");
            Log.d("NotificationReceiver", "Received notification for plant ID: " + plantID);


            // Create the notification
            PPMobileNotificationFactory ppFact = new PPMobileNotificationFactory();
            Notification waterNoti = ppFact.createWaterNotification(plantID, plantName, context);
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.notify(plantID, waterNoti);
        }

}
