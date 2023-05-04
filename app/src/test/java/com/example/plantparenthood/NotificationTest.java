package com.example.plantparenthood;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith (MockitoJUnitRunner.class)
public class NotificationTest {

    private static Context mockedContext;
    private static int plantID;
    private static String plantName;

    @BeforeClass
    public static void setUp() {
        // Mock the application context
        mockedContext = Mockito.mock(Context.class);
        when(mockedContext.getString(R.string.channel_name)).thenReturn("Test channel");
        when(mockedContext.getString(R.string.channel_description)).thenReturn("Test channel description");
        // Set up plant info
        plantID = 123;
        plantName = "Test plant name";

    }

    @Test
    public void testCreateWaterNotification() {

        // Create the factory and call the method
        PPMobileNotificationFactory factory = new PPMobileNotificationFactory();
        if(factory != null){
            Notification result = factory.createWaterNotification(plantID, plantName, mockedContext);

            if (result != null) {
                // Check that the result matches the expected values
                assertEquals(NotificationCompat.PRIORITY_DEFAULT, result.priority);
                assertEquals("Test plant name needs to be watered!", result.extras.getString(NotificationCompat.EXTRA_TITLE));
                assertEquals("Your plant Test plant name needs to be watered!", result.extras.getString(NotificationCompat.EXTRA_TEXT));
            }else{
                fail();
            }

        }else{
            fail();
        }

    }
}
