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
        AsyncTask.execute(() -> DatabaseHandler.getDatabase(null).saveWateringSchedule(wateringSchedule));

    }

    public void removePlantSchedule(Context context, Plant plant)
    {

    }

    public void updatePlantSchedule(Context context, Plant plant)
    {

    }

    public List<Plant> findScheduledPlantsForToday(List<Plant> listOfPlants, int dateOnCalender)
    {
        List<Plant> toBeWateredToday = new ArrayList<>();
        System.out.println("Size of plants list " + listOfPlants.size());
        for (int i = 0; i < listOfPlants.size(); i++)
        {
            Watering water = listOfPlants.get(i).getWateringCycle();
            if(water != null) {
                System.out.println("Plant visited: " + i);
                if (dateOnCalender - water.getLastWateredDay() == water.getWateringInterval())
                {
                    toBeWateredToday.add(listOfPlants.get(i));
                }

                if (todaysDate - water.getLastWateredDay() == water.getWateringInterval())//if user misses their watering, add it to list
                {
                    toBeWateredToday.add(listOfPlants.get(i));
                }
            }
        }

        System.out.println("Number of plants is: " + toBeWateredToday.size());

        return toBeWateredToday;
    }
}
