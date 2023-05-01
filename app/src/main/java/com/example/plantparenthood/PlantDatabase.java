package com.example.plantparenthood;

import android.provider.ContactsContract;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PlantSaveToDatabase.class,Space.class, Watering.class}, version = 1)
public abstract class PlantDatabase extends RoomDatabase
{
    public abstract DataAccessObject dataAccessObject();
    public abstract SpaceDataAccessObject spaceDataAccessObject();
    public abstract WateringDao wateringDao();
}
