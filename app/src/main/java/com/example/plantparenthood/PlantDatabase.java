package com.example.plantparenthood;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PlantSaveToDatabase.class, Group.class, Watering.class}, version = 1)
public abstract class PlantDatabase extends RoomDatabase
{
    public abstract DataAccessObject dataAccessObject();
    public abstract GroupDataAccessObject GroupDataAccessObject();
    public abstract WateringDao wateringDao();
}
