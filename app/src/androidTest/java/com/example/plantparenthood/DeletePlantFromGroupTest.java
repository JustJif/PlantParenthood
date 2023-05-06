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
public class DeletePlantFromGroupTest {
    GroupDataBaseHandler handler;
    Group comparedGroup;
    Plant plant;

    @Before
    public void initializeDatabase() {
        handler = DatabaseHandler.createInMemoryDatabase(ApplicationProvider.getApplicationContext());
        comparedGroup = new Group("Test");
    }

    @Test
    public void createDeleteOnePlantFromGroupTest()
    {
        try {
            Group testGroup = new Group("Test");
            Context context = ApplicationProvider.getApplicationContext();
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


            AsyncTask.execute(() ->
            {

                handler.addGroupToDatabase(testGroup);
                handler.getGroupFromDBbyID(0).addPlant(plant);

                handler.deletePlantFromGroup(testGroup,plant);

                comparedGroup = handler.getGroupFromDBbyID(0);

                assertEquals(testGroup.getAllPlants(), comparedGroup.getAllPlants());
            });
        }
        catch (JSONException e)
        {
            fail();
        }
    }

    @Test
    public void createDeleteNonexistingPlantFromGroupTest()
    {
        try {
            Group testGroup = new Group("Test");
            Context context = ApplicationProvider.getApplicationContext();
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


            AsyncTask.execute(() ->
            {

                handler.addGroupToDatabase(testGroup);

                handler.deletePlantFromGroup(testGroup,plant);

                comparedGroup = handler.getGroupFromDBbyID(0);

                assertEquals(testGroup.getAllPlants(), comparedGroup.getAllPlants());
            });
        }
        catch (JSONException e)
        {
            fail();
        }
    }

    @Test
    public void createDeleteMultiplePlantsFromGroupTest() {
        try {
            Group testGroup = new Group("Test");
            Context context = ApplicationProvider.getApplicationContext();
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
                handler.addGroupToDatabase(testGroup);
                handler.getGroupFromDBbyID(0).addPlant(createdPlants.get(0));
                handler.getGroupFromDBbyID(0).addPlant(createdPlants.get(1));
                handler.getGroupFromDBbyID(0).addPlant(createdPlants.get(2));

                handler.deletePlantFromGroup(testGroup,createdPlants.get(0));
                handler.deletePlantFromGroup(testGroup,createdPlants.get(1));
                handler.deletePlantFromGroup(testGroup,createdPlants.get(2));


                comparedGroup = handler.getGroupFromDBbyID(0);

                assertEquals(testGroup.getAllPlants(), comparedGroup.getAllPlants());
            });
        }
        catch (JSONException e)
        {
            fail();
        }
    }


    @After
    public void cleanUp()
    {
        handler.removeGroupToDatabase(comparedGroup);

    }
}