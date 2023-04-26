/**
 * Abstract PlantParenthood Notification manager.
 */

package com.example.plantparenthood;

import android.app.Notification;
import android.content.Context;

public abstract class AbstractPPNotificationFactory {

    public abstract Notification createWaterNotification(int plantID, String plantName, Context context);

}
