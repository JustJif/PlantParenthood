package com.example.plantparenthood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class PlantCreatorUnitTest
{
    /**
     * Creating exactly one plant from a JSONObject
     * @throws JSONException
     */
    @Test
    public void createSingularPlantTest() throws JSONException
    {
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
        PlantCreator plantCreator = new PlantCreator(null);
        Context mockContext = mock(Context.class);
        ArrayList<Plant> createdPlants = plantCreator.createPlant(lemonGrass,mockContext, null);
        Plant createdPlant = createdPlants.get(0);

        Plant comparedPlant = new Plant.PlantBuilder()
                .setId(2280)
                .setCommon_name("lemon grass")
                .setScientific_name("Cymbopogon citratus")
                .setCycle("Perennial")
                .setWatering("Average")
                .setSunlight("Full sun")
                .setPlantImageURL("https://perenual.com/storage/species_image/2280_cymbopogon_citratus/og/24450188904_50e0c258d6_b.jpg")
                .setDefault_image(BitmapFactory.decodeResource(mockContext.getResources(), R.drawable.defaultimage))
                .buildPlant();

        assertEquals(createdPlant.getId(),              comparedPlant.getId());
        assertEquals(createdPlant.getCommon_name(),     comparedPlant.getCommon_name());
        assertEquals(createdPlant.getScientific_name(), comparedPlant.getScientific_name());
        assertEquals(createdPlant.getOther_name(),      comparedPlant.getOther_name());
        assertEquals(createdPlant.getCycle(),           comparedPlant.getCycle());
        assertEquals(createdPlant.getWatering(),        comparedPlant.getWatering());
        assertEquals(createdPlant.getSunlight(),        comparedPlant.getSunlight());
        assertEquals(createdPlant.getDefault_image(),   comparedPlant.getDefault_image());
        assertEquals(createdPlant.getPlantImageURL(),   comparedPlant.getPlantImageURL());
    }

    /**
     * Creating more than one plant from the JSONObject
     */
    @Test
    public void createMultiplePlantTest() throws JSONException
    {
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

        PlantCreator plantCreator = new PlantCreator(null);
        Context mockContext = mock(Context.class);
        ArrayList<Plant> createdPlants = plantCreator.createPlant(threePlants,mockContext, null);
        ArrayList<Plant> comparedPlants = new ArrayList<>();

        //creating plants with builder pattern to compare
        Plant comparedPlant = new Plant.PlantBuilder()
                .setId(771)
                .setCommon_name("Devil's tongue")
                .setScientific_name("Amorphophallus konjac")
                .setCycle("Perennial")
                .setWatering("Average")
                .setSunlight("Part shade")
                .setPlantImageURL("https://perenual.com/storage/species_image/771_amorphophallus_konjac/og/7375633894_b44dbcd920_b.jpg")
                .setDefault_image(BitmapFactory.decodeResource(mockContext.getResources(), R.drawable.defaultimage))
                .buildPlant();
        comparedPlants.add(comparedPlant);

        comparedPlant = new Plant.PlantBuilder()
                .setId(915)
                .setCommon_name("Devil's walking stick")
                .setScientific_name("Aralia spinosa")
                .setCycle("Perennial")
                .setWatering("Average")
                .setSunlight("Full sun")
                .setPlantImageURL("https://perenual.com/storage/species_image/915_aralia_spinosa/og/33510919603_2564d403a0_b.jpg")
                .setDefault_image(BitmapFactory.decodeResource(mockContext.getResources(), R.drawable.defaultimage))
                .buildPlant();
        comparedPlants.add(comparedPlant);

        comparedPlant = new Plant.PlantBuilder()
                .setId(2284)
                .setCommon_name("Bermuda grass")
                .setScientific_name("Cynodon dactylon 'Sundevil'")
                .setCycle("Perennial")
                .setWatering("Minimum")
                .setSunlight("Full sun")
                .setPlantImageURL("https://perenual.com/storage/species_image/2_abies_alba_pyramidalis/og/49255769768_df55596553_b.jpg")
                .setDefault_image(BitmapFactory.decodeResource(mockContext.getResources(), R.drawable.defaultimage))
                .buildPlant();
        comparedPlants.add(comparedPlant);

        for (int i = 0; i < createdPlants.size(); i++)
        {
            assertEquals(createdPlants.get(i).getId(),              comparedPlants.get(i).getId());
            assertEquals(createdPlants.get(i).getCommon_name(),     comparedPlants.get(i).getCommon_name());
            assertEquals(createdPlants.get(i).getScientific_name(), comparedPlants.get(i).getScientific_name());
            assertEquals(createdPlants.get(i).getOther_name(),      comparedPlants.get(i).getOther_name());
            assertEquals(createdPlants.get(i).getCycle(),           comparedPlants.get(i).getCycle());
            assertEquals(createdPlants.get(i).getWatering(),        comparedPlants.get(i).getWatering());
            assertEquals(createdPlants.get(i).getSunlight(),        comparedPlants.get(i).getSunlight());
            assertEquals(createdPlants.get(i).getDefault_image(),   comparedPlants.get(i).getDefault_image());
            assertEquals(createdPlants.get(i).getPlantImageURL(),   comparedPlants.get(i).getPlantImageURL());
        }
    }

    /**
     * When the API would return an invalid query, no plant should be created
     * and the case would be handled
     */
    @Test
    public void createNullPlantTest() throws JSONException
    {
        JSONObject jsonObject = new JSONObject("{\"data\":[],\"to\":null,\"per_page\":30,\"current_page\":1,\"from\":null,\"last_page\":1,\"total\":0}");

        PlantCreator plantCreator = new PlantCreator(null);
        Context mockContext = mock(Context.class);
        ArrayList<Plant> createdPlants = plantCreator.createPlant(jsonObject,mockContext, null);

        assertEquals(createdPlants.size(), 0);
    }
}
