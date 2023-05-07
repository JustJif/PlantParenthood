package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.content.Context;
import android.os.AsyncTask;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
@RunWith(AndroidJUnit4.class)

public class AddPlantScheduleTest {

    DatabaseHandler handler;
    Plant plant;
    Schedule plantSchedule;

    /**
     * A plant, and a plant schedule is required prior to being able to delete
     */
    @Before
    public void initializeSchedule()
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

            plantSchedule = new Schedule();
            plantSchedule.addPlantSchedule(plant,1);
        }
        catch (JSONException e)
        {
            fail(); //forcefully fails if the exception is thrown
        }
    }

    /**
     * Update the planet schedule from 1 day, to 2 days
     */
    @Test
    public void updatePlantScheduleFrom1to2()
    {
        Watering wateringSchedule = plant.getWateringCycle();
        assertEquals(wateringSchedule.getWateringInterval(), 1);

    }

    /** Cleanup database afterward to ensure next run of tests don't have interfering objects
     *
     */
    @After
    public void cleanUp()
    {
        handler.deletePlant(plant);
    }
}



