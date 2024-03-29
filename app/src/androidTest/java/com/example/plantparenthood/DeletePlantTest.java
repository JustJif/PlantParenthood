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

public class DeletePlantTest {

    DatabaseHandler handler;
    List<Plant> comparedPlants;

    @Before
    public void initializeDatabase() {
        handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
        comparedPlants = new ArrayList<>();
    }

    @Test
    public void createSingularPlantTest()
    {
        try {
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
            ArrayList<Plant> createdPlants = plantCreator.createPlant(lemonGrass, context, null);

            AsyncTask.execute(() ->
            {
                Plant createdPlant = createdPlants.get(0);
                plantCreator.addPlant(createdPlant);


                comparedPlants.add(handler.getPlantFromDBbyID(0));//load plant from DB and compare

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
        catch (JSONException e)
        {
            fail(); //forcefully fails if the exception is thrown
        }
    }


    @Test
    public void deletePlantTest() {
        try {
            // Create a plant to be deleted
            JSONObject lemonGrass = new JSONObject("{\"data\":[{\"id\":2280,\"common_name\":\"lemon grass\"," +
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
            ArrayList<Plant> createdPlants = plantCreator.createPlant(lemonGrass, context, null);

            // Add the plant to the database
            Plant createdPlant = createdPlants.get(0);
            plantCreator.addPlant(createdPlant);

            // Delete the plant from the database
            handler.deletePlant(createdPlant.getId());


        } catch (JSONException e) {
            fail(); // forcefully fails if the exception is thrown
        }
    }


}
