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
import java.util.Date;
import java.util.TimeZone;

public class Schedule {
    private int interval;
    private Date startDate;
    private Date endDate;

    public Schedule(int interval, Date startDate, Date endDate) {
        this.interval = interval;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addPlantSchedule(Context context, Plant plant) {
        // add plant watering schedule to calendar
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, "Water " + plant.getCommon_name());
        event.put(CalendarContract.Events.DESCRIPTION, "Water " + plant.getCommon_name() + " every " + interval + " days.");
        event.put(CalendarContract.Events.DTSTART, startDate.getTime());
        event.put(CalendarContract.Events.DTEND, endDate.getTime());
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        event.put(CalendarContract.Events.CALENDAR_ID, 1);
        event.put(CalendarContract.Events.EVENT_COLOR, Color.GREEN);

        Uri eventsUri = CalendarContract.Events.CONTENT_URI;
        Uri uri = context.getContentResolver().insert(eventsUri, event);

        // store the event ID in the plant object
        long eventId = Long.parseLong(uri.getLastPathSegment());
        //plant.setEventId(eventId);

        // set the watering interval for the plant
        //plant.setWateringInterval(interval);
    }

   /* public void deletePlantSchedule(Context context, Plant plant) {
        // delete plant watering schedule from calendar
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, plant.getEventId());
            int rows = contentResolver.delete(deleteUri, null, null);
            System.out.println(rows + " row(s) deleted");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void updatePlantSchedule(Context context, Plant plant) {
        // update plant watering schedule on calendar
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, "Water " + plant.getCommon_name());
        event.put(CalendarContract.Events.DESCRIPTION, "Water " + plant.getCommon_name() + " every " + interval + " days.");
        event.put(CalendarContract.Events.DTSTART, startDate.getTime());
        event.put(CalendarContract.Events.DTEND, endDate.getTime());
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        event.put(CalendarContract.Events.EVENT_COLOR, Color.GREEN);

        //Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, plant.getEventId());
        int rows = context.getContentResolver().update(updateUri, event, null, null);
        System.out.println(rows + " row(s) updated");
    }

    */
}
