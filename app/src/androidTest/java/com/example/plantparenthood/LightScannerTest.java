package com.example.plantparenthood;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.app.NotificationCompat;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LightScannerTest {

    LightScannerActivity lightScannerActivity;

    @Before
    public void initializeLightScanner()
    {
        lightScannerActivity = new LightScannerActivity();
    }

    @Test
    public void testBrightnessCalculator() throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_4444);
        File file = new File("test");
        FileOutputStream fOut = new FileOutputStream(file);

        newBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        lightScannerActivity.calculateLightLevel(file);
        fOut.flush();
        fOut.close();
    }
}