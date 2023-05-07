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

public class FilterPlantTest {


        @Test
        void testFilterPlants() {
            List<Plant> plants = new ArrayList<>();
            plants.add(new Plant(true, false, false, false, false, false, false, false, false, false, false, false));
            plants.add(new Plant(false, true, false, false, false, false, false, false, false, false, false, false));
            plants.add(new Plant(false, false, true, false, false, false, false, false, false, false, false, false));
            plants.add(new Plant(false, false, false, true, false, false, false, false, false, false, false, false));
            plants.add(new Plant(false, false, false, false, true, false, false, false, false, false, false, false));
            plants.add(new Plant(false, false, false, false, false, true, false, false, false, false, false, false));
            plants.add(new Plant(false, false, false, false, false, false, true, false, false, false, false, false));
            plants.add(new Plant(false, false, false, false, false, false, false, true, false, false, false, false));
            plants.add(new Plant(false, false, false, false, false, false, false, false, true, false, false, false));
            plants.add(new Plant(false, false, false, false, false, false, false, false, false, true, false, false));
            plants.add(new Plant(false, false, false, false, false, false, false, false, false, false, true, false));
            plants.add(new Plant(false, false, false, false, false, false, false, false, false, false, false, true));

            List<Plant> filteredPlants = filterPlants(plants, true, false, true, false, false, false, false, false, false, false, false, false);
            assertEquals(2, filteredPlants.size());
            assertEquals(true, filteredPlants.get(0).isFullShade());
            assertEquals(true, filteredPlants.get(1).isAverage());

            filteredPlants = filterPlants(plants, false, false, false, false, true, false, false, false, false, false, false, false);
            assertEquals(1, filteredPlants.size());
            assertEquals(true, filteredPlants.get(0).isPerennial());

            filteredPlants = filterPlants(plants, false, false, false, false, false, false, true, true, true, false, false, false);
            assertEquals(3, filteredPlants.size());
            assertEquals(true, filteredPlants.get(0).isFullSun());
            assertEquals(true, filteredPlants.get(1).isSunPartShade());
            assertEquals(true, filteredPlants.get(2).isPartShade());

    }

}
