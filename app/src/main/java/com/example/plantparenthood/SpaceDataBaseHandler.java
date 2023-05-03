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
        spaceDB = Room.databaseBuilder(context, PlantDatabase.class, "PlantDatabase").build();
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
        Space loadedSpace = spaceDB.spaceDataAccessObject().loadSpaceByID(spaceID);
        ArrayList<Plant> plantArrayList = new ArrayList<Plant>();
        loadPlantsIntoSpace(loadedSpace.getPlantIDs(),plantArrayList);
        loadedSpace.setAllPlants(plantArrayList);
        return loadedSpace;
    }

    private void loadPlantsIntoSpace(String unparsed, ArrayList<Plant> plantArrayList) {
        String[] parsed = unparsed.split(",");
        for(int i = 0; i < parsed.length; i++) {
            plantArrayList.add(DatabaseHandler.getDatabase(null).getPlantFromDBbyID(Integer.parseInt(parsed[i])));
        }
    }

    public List<Space> getSpacesFromDB()
    {
        List<Space> loadedSpaces = spaceDB.spaceDataAccessObject().loadAllSpaces();
        return loadedSpaces;
    }

    public void addSpaceToDatabase(Space space)
    {
        spaceDB.spaceDataAccessObject().addSpace(space);
        System.out.println(getSpacesFromDB().size());
    }
}
