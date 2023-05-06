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
public class AddPlantTest {
    DatabaseHandler handler;
    List<Plant> comparedPlants;

    @Before
    public void initializeDatabase() {
        handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
        comparedPlants = new ArrayList<>();
    }

    /**
     * Creating exactly one plant from a JSONObject
     */
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

    /**
     * Creating more than one plant from the JSONObject
     */
    @Test
    public void createMultiplePlantTest() {
        try {
            JSONObject threePlants = new JSONObject("{\"data\":[{\"id\":771,\"common_name\":\"Devil's tongue\",\"scientific_name\":" +
                    "[\"Amorphophallus konjac\"],\"other_name\":[],\"cycle\":\"Perennial\",\"watering\":\"Average\",\"sunlight\":[\"Part shade\"]," +
                    "\"default_image\":{\"license\":4,\"license_name\":\"Attribution License\",\"license_url\":\"https:\\/\\/creativecommons.org\\" +
                    "/licenses\\/by\\/2.0\\/\",\"original_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/771_amorphophallus_konjac\\/og\\" +
                    "/7375633894_b44dbcd920_b.jpg\",\"regular_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/771_amorphophallus_konjac\\" +
                    "/regular\\/7375633894_b44dbcd920_b.jpg\",\"medium_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/771_amorphophallus_konjac\\" +
                    "/medium\\/7375633894_b44dbcd920_b.jpg\",\"small_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\" +
                    "/771_amorphophallus_konjac\\/small\\/7375633894_b44dbcd920_b.jpg\",\"thumbnail\":\"https:\\/\\/perenual.com\\/storage\\/species_image" +
                    "\\/771_amorphophallus_konjac\\/thumbnail\\/7375633894_b44dbcd920_b.jpg\"}},{\"id\":915,\"common_name\":\"Devil's walking stick\"," +
                    "\"scientific_name\":[\"Aralia spinosa\"],\"other_name\":[],\"cycle\":\"Perennial\",\"watering\":\"Average\",\"sunlight\":[\"Full sun\"," +
                    "\"part shade\"],\"default_image\":{\"license\":5,\"license_name\":\"Attribution-ShareAlike License\",\"license_url\":" +
                    "\"https:\\/\\/creativecommons.org\\/licenses\\/by-sa\\/2.0\\/\",\"original_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\" +
                    "/915_aralia_spinosa\\/og\\/33510919603_2564d403a0_b.jpg\",\"regular_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\" +
                    "/915_aralia_spinosa\\/regular\\/33510919603_2564d403a0_b.jpg\",\"medium_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\" +
                    "/915_aralia_spinosa\\/medium\\/33510919603_2564d403a0_b.jpg\",\"small_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\" +
                    "/915_aralia_spinosa\\/small\\/33510919603_2564d403a0_b.jpg\",\"thumbnail\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\" +
                    "/915_aralia_spinosa\\/thumbnail\\/33510919603_2564d403a0_b.jpg\"}},{\"id\":2284,\"common_name\":\"Bermuda grass\",\"scientific_name\":" +
                    "[\"Cynodon dactylon 'Sundevil'\"],\"other_name\":[],\"cycle\":\"Perennial\",\"watering\":\"Minimum\",\"sunlight\":[\"Full sun\"]," +
                    "\"default_image\":{\"license\":5,\"license_name\":\"Attribution-ShareAlike License\",\"license_url\":\"https:\\/\\/creativecommons.org\\" +
                    "/licenses\\/by-sa\\/2.0\\/\",\"original_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/2_abies_alba_pyramidalis\\/og\\" +
                    "/49255769768_df55596553_b.jpg\",\"regular_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/2_abies_alba_pyramidalis\\" +
                    "/regular\\/49255769768_df55596553_b.jpg\",\"medium_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/2_abies_alba_pyramidalis\\" +
                    "/medium\\/49255769768_df55596553_b.jpg\",\"small_url\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/2_abies_alba_pyramidalis\\/" +
                    "small\\/49255769768_df55596553_b.jpg\",\"thumbnail\":\"https:\\/\\/perenual.com\\/storage\\/species_image\\/2_abies_alba_pyramidalis\\" +
                    "/thumbnail\\/49255769768_df55596553_b.jpg\"}}],\"to\":3,\"per_page\":30,\"current_page\":1,\"from\":1,\"last_page\":1,\"total\":3}");

            PlantCreator plantCreator = new PlantCreator();
            Context context = ApplicationProvider.getApplicationContext();
            AsyncTask.execute(() ->
            {
                ArrayList<Plant> createdPlants = plantCreator.createPlant(threePlants, context, null);
                comparedPlants = handler.getPlantsFromDB();

                for (int i = 0; i < createdPlants.size(); i++) {
                    assertEquals(createdPlants.get(i).getId(), comparedPlants.get(i).getId());
                    assertEquals(createdPlants.get(i).getCommon_name(), comparedPlants.get(i).getCommon_name());
                    assertEquals(createdPlants.get(i).getScientific_name(), comparedPlants.get(i).getScientific_name());
                    assertEquals(createdPlants.get(i).getOther_name(), comparedPlants.get(i).getOther_name());
                    assertEquals(createdPlants.get(i).getCycle(), comparedPlants.get(i).getCycle());
                    assertEquals(createdPlants.get(i).getWatering(), comparedPlants.get(i).getWatering());
                    assertEquals(createdPlants.get(i).getSunlight(), comparedPlants.get(i).getSunlight());
                    assertEquals(createdPlants.get(i).getPlantImageURL(), comparedPlants.get(i).getPlantImageURL());
                }
            });
        }
        catch (JSONException e)
        {
            fail(); //forcefully fails if the exception is thrown
        }
    }

    /**
     * When the API would return an invalid query, no plant should be created
     * and the case would be handled
     */
    @Test
    public void createNullPlantTest() {
        try {
            JSONObject jsonObject = new JSONObject("{\"data\":[],\"to\":null,\"per_page\":30,\"current_page\":1,\"from\":null,\"last_page\":1,\"total\":0}");

            PlantCreator plantCreator = new PlantCreator();
            Context context = ApplicationProvider.getApplicationContext();
            ArrayList<Plant> createdPlants = plantCreator.createPlant(jsonObject, context, null);

            assertEquals(createdPlants.size(), 0);

            AsyncTask.execute(() ->
            {
                comparedPlants = handler.getPlantsFromDB();
                assertEquals(comparedPlants.size(), 0); //check if database has 0 plants in it
            });
        }
        catch (JSONException e)
        {
            fail(); //forcefully fails if the exception is thrown
        }
    }

    /**
     * Cleanup database afterward to ensure next run of tests don't have interfering objects
     */
    @After
    public void cleanUp()
    {
        for (int i = 0; i < comparedPlants.size(); i++) {
            handler.deletePlant(comparedPlants.get(i));
        }
    }
}
