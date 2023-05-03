package com.example.plantparenthood;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class NotificationTest {

    private static Context mockedContext;

    @BeforeClass
    public static void setUp() {
        // Mock the application context
        mockedContext = Mockito.mock(Context.class);
        when(mockedContext.getString(R.string.channel_name)).thenReturn("Test channel");
        when(mockedContext.getString(R.string.channel_description)).thenReturn("Test channel description");
    }

    @Test
    public void testCreateWaterNotification() {
        // Set up inputs
        int plantID = 123;
        String plantName = "Test plant name";

        // Create the factory and call the method
        PPMobileNotificationFactory factory = new PPMobileNotificationFactory();
        if(factory != null){
            Notification result = factory.createWaterNotification(plantID, plantName, mockedContext);

            // Check that the result matches the expected values
            assertEquals(NotificationCompat.PRIORITY_DEFAULT, result.priority);
            assertEquals("Test plant name needs to be watered!", result.extras.getString(NotificationCompat.EXTRA_TITLE));
            assertEquals("Your plant Test plant name needs to be watered!", result.extras.getString(NotificationCompat.EXTRA_TEXT));
        }

    }
}
