/**
 * Abstract PlantParenthood Notification manager.
 */

package com.example.plantparenthood;

import android.app.Notification;
import android.content.Context;

public abstract class AbstractPPNotificationFactory {

    public abstract  Object createWaterNotification(int plantID, String plantName, Context context);

}
