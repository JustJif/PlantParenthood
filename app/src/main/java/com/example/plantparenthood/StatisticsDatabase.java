package com.example.plantparenthood;

import android.provider.ContactsContract;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Statistics.class}, version = 1)
public abstract class StatisticsDatabase extends RoomDatabase
{
    public abstract StatisticsAccessObject statisticsAccessObject();
}
