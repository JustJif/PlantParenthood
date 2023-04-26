package com.example.plantparenthood;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Follows singleton pattern, only call methods in here using async task,
 * as database cannot be accesses on the main thread
 */
public class SpaceDataBaseHandler {
    private static SpaceDataBaseHandler activeDatabase = null;
    private PlantDatabase spaceDB;

    private SpaceDataBaseHandler(Context context)
    {
        spaceDB = Room.databaseBuilder(context, PlantDatabase.class, "SpaceDatabase").build();
    }

    public static SpaceDataBaseHandler getDatabase(Context applicationContext)
    {
        if(activeDatabase == null)
        {
            activeDatabase = new SpaceDataBaseHandler(applicationContext);
        }

        return activeDatabase;
    }

    public SpaceDataAccessObject getDataAccessObject()
    {
        return spaceDB.spaceDataAccessObject();
    }

    public Space getSpaceFromDBbyID(int spaceID)
    {
         loadedSpace = spaceDB.spaceDataAccessObject().loadSpaceByID(spaceID);
        return loadedSpace;
    }

    public List<Plant> getPlantsFromDB()
    {
        List<PlantSaveToDatabase> loadedPlants = plantDB.dataAccessObject().loadAllPlants();
        List<Plant> formattedPlantOutput = new ArrayList<>();
        for (int i = 0; i < loadedPlants.size(); i++)
            formattedPlantOutput.add(plantCreator.createPlantFromDatabase(loadedPlants.get(i)));

        return formattedPlantOutput;
    }

    public void addSpaceToDatabase(Space space)
    {
        spaceDB.spaceDataAccessObject().addSpace(space);
    }
}
