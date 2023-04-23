package com.example.plantparenthood;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.TimeZone;

public class Schedule {
    private int interval;
    private long startDate;
    private long endDate;

    public Schedule(int interval, long startDate, long endDate) {
        this.interval = interval;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public void addPlantSchedule(Context context, Plant plant) {
        // add plant watering schedule to calendar
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, "Water " + plant.getCommon_name());
        event.put(CalendarContract.Events.DESCRIPTION, "Water " + plant.getCommon_name() + " every " + interval + " days.");
        event.put(CalendarContract.Events.DTSTART, startDate);
        event.put(CalendarContract.Events.DTEND, endDate);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        event.put(CalendarContract.Events.CALENDAR_ID, 1);
        event.put(CalendarContract.Events.EVENT_COLOR, Color.GREEN);

        Uri eventsUri = CalendarContract.Events.CONTENT_URI;
        context.getContentResolver().insert(eventsUri, event);
    }

    public void deletePlantSchedule(Context context, Plant plant) {
        // delete plant watering schedule from calendar
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = CalendarContract.Events.CONTENT_URI;
            String[] projection = new String[]{CalendarContract.Events._ID};
            String selection = CalendarContract.Events.TITLE + "=?";
            String[] selectionArgs = new String[]{ "Water " + plant.getCommon_name() };
            cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);
            while (cursor.moveToNext()) {
                long eventId = cursor.getLong(0);
                Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
                contentResolver.delete(deleteUri, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void updatePlantSchedule(Context context, Plant plant) {
        // update plant watering schedule in calendar
        deletePlantSchedule(context, plant);
        addPlantSchedule(context, plant);
    }




}
