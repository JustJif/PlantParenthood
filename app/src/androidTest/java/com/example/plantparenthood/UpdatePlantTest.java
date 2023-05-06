package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UpdatePlantTest
{
    DatabaseHandler handler;
    Plant plant;

    @Before
    public void initializeDatabase()
    {
        try {
            handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
            JSONObject lemonGrass = new JSONObject
                    ("{\"data\":[{\"id\":2280,\"common_name\":\"lemon grass\"," +
                            "\"scientific_name\":[\"Cymbopogon citratus\"],\"other_name\":[]," +
                            "\"cycle\":\"Perennial\",\"watering\":\"Average\",\"sunlight\"" +
                            ":[\"Full sun\"],\"default_image\":{\"license\":4,\"license_name\"" +
                            ":\"Attribution License\",\"license_url\":\"https:\\/\\/creativecommons.org" +
                            "\\/licenses\\/by\\/2.0\\/\",\"original_url\":\"https:\\/\\/perenual.com" +
                            "\\/storage\\/species_image\\/2280_cymbopogon_citratus\\/og\\/24450188904_50e0c258d6_b.jpg\"," +
                            "\"regular_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/2280_cymbopogon_citratus\\" +
                            "/regular\\/24450188904_50e0c258d6_b.jpg\",\"medium_url\":\"https:\\/\\/perenual.com\\/storage\\" +
                            "/species_image\\/2280_cymbopogon_citratus\\/medium\\/24450188904_50e0c258d6_b.jpg\"," +
                            "\"small_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/2280_cymbopogon_citratus\\" +
                            "/small\\/24450188904_50e0c258d6_b.jpg\",\"thumbnail\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\" +
                            "/2280_cymbopogon_citratus\\/thumbnail\\/24450188904_50e0c258d6_b.jpg\"}}],\"to\":1,\"per_page\":30,\"" +
                            "current_page\":1,\"from\":1,\"last_page\":1,\"total\":1}"
                    );
            PlantCreator plantCreator = new PlantCreator();
            Context context = ApplicationProvider.getApplicationContext();
            plant = plantCreator.createPlant(lemonGrass, context, null).get(0);
        }
        catch (JSONException e)
        {
            fail(); //forcefully fails if the exception is thrown
        }
    }

    /**
     * Updating only one parameter from the plant
     */
    @Test
    public void updatePlantCommonName()
    {
        boolean[] changes = new boolean[]{true,false,false};
        String[] data = {"Updated name",""};

        AsyncTask.execute(() ->
        {
            plant.updatePlant(changes, data, null);
            Plant newPlant = handler.getPlantFromDBbyID(0);

            assertEquals(plant.getId(),              newPlant.getId());
            assertEquals(plant.getCommon_name(),     newPlant.getCommon_name());
            assertEquals(plant.getScientific_name(), newPlant.getScientific_name());
        });
    }

    /**
     * Updating two name parameters from the plant
     */
    @Test
    public void updatePlantNames()
    {
        boolean[] changes = new boolean[]{true,true,false};
        String[] data = {"Updated name","Other name"};

        AsyncTask.execute(() ->
        {
            plant.updatePlant(changes, data, null);
            Plant newPlant = handler.getPlantFromDBbyID(0);

            assertEquals(plant.getId(),              newPlant.getId());
            assertEquals(plant.getCommon_name(),     newPlant.getCommon_name());
            assertEquals(plant.getScientific_name(), newPlant.getScientific_name());
        });
    }

    /**
     * Updating all parameters from the plant
     */
    @Test
    public void updatePlantAllChanges()
    {
        boolean[] changes = new boolean[]{true,true,true};
        String[] data = {"Updated name","Other name"};
        Bitmap newImage = BitmapFactory.decodeResource(ApplicationProvider.getApplicationContext().getResources(), R.drawable.defaultimage);
        AsyncTask.execute(() ->
        {
            plant.updatePlant(changes, data, newImage);
            Plant newPlant = handler.getPlantFromDBbyID(0);

            assertEquals(plant.getId(),              newPlant.getId());
            assertEquals(plant.getCommon_name(),     newPlant.getCommon_name());
            assertEquals(plant.getScientific_name(), newPlant.getScientific_name());
            assertEquals(plant.getDefault_image(),   newPlant.getDefault_image());
            assertEquals(plant.getPlantImageURL(),   newPlant.getPlantImageURL());
        });
    }

    /**
     * Updating no parameters from plant
     */
    @Test
    public void updatePlantNoChanges()
    {
        boolean[] changes = new boolean[]{false,false,false};
        String[] data = {"",""};

        AsyncTask.execute(() ->
        {
            plant.updatePlant(changes, data, null);
            Plant newPlant = handler.getPlantFromDBbyID(0);

            assertEquals(plant.getId(),              newPlant.getId());
            assertEquals(plant.getCommon_name(),     newPlant.getCommon_name());
            assertEquals(plant.getScientific_name(), newPlant.getScientific_name());
            assertEquals(plant.getOther_name(),      newPlant.getOther_name());
            assertEquals(plant.getCycle(),           newPlant.getCycle());
            assertEquals(plant.getWatering(),        newPlant.getWatering());
            assertEquals(plant.getSunlight(),        newPlant.getSunlight());
            assertEquals(plant.getPlantImageURL(),   newPlant.getPlantImageURL());
        });
    }

    /**
     * Cleanup database afterward to ensure next run of tests don't have interfering objects
     */
    @After
    public void cleanUp()
    {
        handler.deletePlant(plant);
    }
}
