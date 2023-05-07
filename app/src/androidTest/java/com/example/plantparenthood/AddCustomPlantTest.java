package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;
import android.os.AsyncTask;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)

public class AddCustomPlantTest {

    DatabaseHandler handler;
    List<Plant> comparedPlants;

    @Before
    public void initializeDatabase() {
        handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
        comparedPlants = new ArrayList<>();
    }

    @Test
    public void addCustomPlantTest() {
        // Create a custom plant
        Plant customPlant = new Plant.PlantBuilder()
        customPlant.setCommon_name("Custom Plant");
        customPlant.setScientific_name("Customus plantus");
        customPlant.setCycle("Annual");
        customPlant.setWatering("High");
        customPlant.setSunlight("Full sun");

        PlantCreator plantCreator = new PlantCreator();
        Context context = ApplicationProvider.getApplicationContext();
        ArrayList<Plant> createdPlants = plantCreator.createPlant(customPlant, context, null);

        AsyncTask.execute(() -> {
            Plant createdPlant = createdPlants.get(0);
            plantCreator.addPlant(createdPlant);

            // Load plant from DB and compare
            comparedPlants.add(handler.getPlantFromDBbyID(0));
            assertEquals(createdPlant.getId(), comparedPlants.get(0).getId());
            assertEquals(createdPlant.getCommon_name(), comparedPlants.get(0).getCommon_name());
            assertEquals(createdPlant.getScientific_name(), comparedPlants.get(0).getScientific_name());
            assertEquals(createdPlant.getOther_name(), comparedPlants.get(0).getOther_name());
            assertEquals(createdPlant.getCycle(), comparedPlants.get(0).getCycle());
            assertEquals(createdPlant.getWatering(), comparedPlants.get(0).getWatering());
            assertEquals(createdPlant.getSunlight(), comparedPlants.get(0).getSunlight());
            assertEquals(createdPlant.getPlantImageURL(), comparedPlants.get(0).getPlantImageURL());
        });
    }



}
