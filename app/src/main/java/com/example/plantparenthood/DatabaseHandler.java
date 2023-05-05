package com.example.plantparenthood;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Follows singleton pattern, only call methods in here using async task,
 * as database cannot be accesses on the main thread
 */
public class DatabaseHandler {
    private static DatabaseHandler activeDatabase = null;
    private PlantCreator plantCreator;
    private PlantDatabase plantDB;
    private DatabaseHandler(Context context)
    {
        plantDB = Room.databaseBuilder(context, PlantDatabase.class, "PlantDatabase").build();
        plantCreator = new PlantCreator();
    }

    public static DatabaseHandler getDatabase(Context applicationContext)
    {
        if(activeDatabase == null)
        {
            activeDatabase = new DatabaseHandler(applicationContext);
        }

        return activeDatabase;
    }

    public static DatabaseHandler getDatabase()
    {
        return activeDatabase;
    }

    public DataAccessObject getDataAccessObject()
    {
        return plantDB.dataAccessObject();
    }

    public Plant getPlantFromDBbyID(int plantID)
    {
        PlantSaveToDatabase loadedPlant = plantDB.dataAccessObject().loadPlantByID(plantID);
        Plant plant = plantCreator.createPlantFromDatabase(loadedPlant, attachWateringSchedule(loadedPlant.getId()));

        return plant;
    }

    public List<Plant> getPlantsFromDB()
    {
        List<PlantSaveToDatabase> loadedPlants = plantDB.dataAccessObject().loadAllPlants();
        List<Plant> formattedPlantOutput = new ArrayList<>();
        for (int i = 0; i < loadedPlants.size(); i++) {
            PlantSaveToDatabase thisPlant = loadedPlants.get(i);
            formattedPlantOutput.add(plantCreator.createPlantFromDatabase(thisPlant, attachWateringSchedule(thisPlant.getId())));
        }

        return formattedPlantOutput;
    }

    public void addPlantToDatabase(Plant plant)
    {
        PlantSaveToDatabase newPlant = new PlantSaveToDatabase(plant);
        plantDB.dataAccessObject().addPlant(newPlant);
    }

    /**
     * As the database cannot save objects, watering schedule is fetched separately
     * query the id of plantID, it may or may not be valid
     */
    private Watering attachWateringSchedule(int plantID)
    {
        return plantDB.wateringDao().loadWateringByID(plantID);
    }

    public void saveWateringSchedule(Watering watering)
    {
        plantDB.wateringDao().addWatering(watering);
    }

    public void deleteWateringSchedule(Watering watering)
    {
     plantDB.wateringDao().deleteSchedule(watering);
    }
}
