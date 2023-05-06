package com.example.plantparenthood;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StatisticsTest {

    StatisticsDatabaseHandler databaseHandler;
    StatisticsManager statisticsManager;
    /**
     * Pre-initialize a database
     */
    @Before
    public void initializeDatabase()
    {
        databaseHandler = StatisticsDatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
        statisticsManager = new StatisticsManager();
    }

    @Test
    public void testAddPlant()
    {
        statisticsManager.addPlant();
        statisticsManager.getTotalOwnedPlants();

        Assert.assertEquals(statisticsManager.getNumOwnedPlants(),1);
        Assert.assertEquals(statisticsManager.getTotalOwnedPlants(),1);
    }

    @Test
    public void testWaterPlant(){
        statisticsManager.waterPlant();

        Assert.assertEquals(statisticsManager.getTotalTimesWatered(),1);
        Assert.assertEquals(statisticsManager.getFirstTimeWatered(),statisticsManager.getLastTimeWatered());
    }

    @Test
    public void testDeletePlant(){
        statisticsManager.addPlant();
        statisticsManager.deletePlant();

        Assert.assertEquals(statisticsManager.getTotalDeadPlants(),1);
        Assert.assertEquals(statisticsManager.getTotalOwnedPlants(),1);
    }
}