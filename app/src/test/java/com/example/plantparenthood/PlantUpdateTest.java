package com.example.plantparenthood;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PlantUpdateTest
{
    Plant plantObj;
    @Before
    public void initializePlantObject()
    {
        JSONObject lemonGrass = null;
        try {
            lemonGrass = new JSONObject
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
        } catch (JSONException e) {
            fail("JSONObject creation failed");
        }

        PlantCreator plantCreator = new PlantCreator(null);
        Context mockContext = mock(Context.class);
        ArrayList<Plant> createdPlants = plantCreator.createPlant(lemonGrass,mockContext, null);
        Plant createdPlant = createdPlants.get(0);
    }

    /**
     * This tests for a plant that had its data changed
     */
    @Test
    public void updatePlantTest()
    {

    }
}
