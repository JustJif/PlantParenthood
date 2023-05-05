package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.plantparenthood.PPMobileNotificationFactory;
import com.example.plantparenthood.R;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NotificationTest {

    private static int plantID;
    private static String plantName;
    private static Context context;

    @BeforeClass
    public static void setUp() {
        // Set up plant info
        plantID = 123;
        plantName = "Test plant name";
        // Get a Context object for the app under test1
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    }

    @Test
    public void testCreateWaterNotification() {
    // Create the factory and call the method
    PPMobileNotificationFactory factory = new PPMobileNotificationFactory();

    Notification result = factory.createWaterNotification(plantID, plantName,context );


    // Check that the result matches the expected values
    assertEquals(NotificationCompat.PRIORITY_DEFAULT, result.priority);
    assertEquals("Test plant name needs to be watered!", result.extras.getString(NotificationCompat.EXTRA_TITLE));
    assertEquals("Your plant Test plant name needs to be watered!", result.extras.getString(NotificationCompat.EXTRA_TEXT));

    }
}