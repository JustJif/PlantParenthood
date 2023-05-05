package com.example.plantparenthood;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Schedule
{
    int todaysDate;
    public Schedule()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            todaysDate = LocalDateTime.now().getDayOfYear();
    }

    public void addPlantSchedule(Plant plant, int wateringInterval)
    {
        Watering wateringSchedule = new Watering(plant.getId(), todaysDate, wateringInterval, 0);
        plant.setWateringCycle(wateringSchedule);
        AsyncTask.execute(() -> DatabaseHandler.getDatabase().saveWateringSchedule(wateringSchedule));
    }

    /**
     * Removes a schedule from a plant, its associated via the primary key
     * within the watering to know the plant it belongs to.
     * @param water the watering schedule to remove
     */
    public void deletePlantSchedule(Watering water)
    {
        water.deleteWateringSchedule();
    }

    /**
     * update plant watering schedule.
     * Call this method async, due to database access
     * @param thisPlant the plant watering schedule to modify
     * @param newValue the new value of the watering interval;
     */
    public void updatePlantSchedule(Plant thisPlant, int newValue)
    {
        thisPlant.getWateringCycle().setWateringInterval(newValue);
        DatabaseHandler.getDatabase().saveWateringSchedule(thisPlant.getWateringCycle());
    }

    public List<Plant> findScheduledPlantsForToday(List<Plant> listOfPlants, int dateOnCalender)
    {
        List<Plant> toBeWateredToday = new ArrayList<>();
        for (int i = 0; i < listOfPlants.size(); i++)
        {
            Watering water = listOfPlants.get(i).getWateringCycle();
            if(water != null) {
                if (dateOnCalender - water.getLastWateredDay() == water.getWateringInterval())
                    toBeWateredToday.add(listOfPlants.get(i));

                if (todaysDate - water.getLastWateredDay() > water.getWateringInterval())//if user misses their watering, add it to list
                    toBeWateredToday.add(listOfPlants.get(i));
            }
        }

        System.out.println("Number of plants is: " + toBeWateredToday.size());
        return toBeWateredToday;
    }
}
