package com.example.plantparenthood;

import android.content.Context;
import android.widget.ImageView;

import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlantCreator
{
    public static void addPlant(JSONObject nonparsedPlants, PlantSearcher removethis) throws JSONException
    {
        ArrayList<Plant> createdPlantObjects = new ArrayList<>();
        JSONArray plantsList = nonparsedPlants.getJSONArray("data");

        for (int i = 0; i < plantsList.length(); i++)
        {
            JSONObject currentPlant = plantsList.getJSONObject(i);
            int id = currentPlant.getInt("id");
            String common_name = currentPlant.getString("common_name");
            JSONArray scientific_name = currentPlant.getJSONArray("scientific_name");
            JSONArray other_name = currentPlant.getJSONArray("other_name");
            String cycle = currentPlant.getString("cycle");
            String watering = currentPlant.getString("watering");
            JSONArray sunlight = currentPlant.getJSONArray("sunlight");
            JSONObject default_image = currentPlant.getJSONObject("default_image");


            String[] plantScientificNames = new String[scientific_name.length()];
            for (int j = 0; j < scientific_name.length(); j++)
            {
                plantScientificNames[j] = scientific_name.getString(j);
            }

            /*String[] plantOtherNames = new String[other_name.length()];
            for (int k = 0; k < scientific_name.length(); k++)
            {
                plantOtherNames[k] = other_name.getString(k);
            }*/

            //string builder stuff here...
            Plant newPlant = new Plant();
            newPlant.id = id;
            newPlant.common_name = common_name;
            newPlant.scientific_name = plantScientificNames[0];
            //newPlant.other_name = other_name;
            newPlant.cycle = cycle;
            newPlant.watering = watering;
            //newPlant.sunlight = sunlight;
            newPlant.plantImageURL = default_image.getString("original_url");

            createdPlantObjects.add(newPlant);
        }

        Integer currentPage = nonparsedPlants.getInt("current_page");
        Integer lastPage = nonparsedPlants.getInt("last_page");
        removethis.createPlantGrid(createdPlantObjects,currentPage,lastPage);
    }

    public static void addCustomPlant()
    {

    }

    public static void addPlantToDatabase(Plant plant, Context context)
    {
        PlantDatabase plantDatabase = Room.databaseBuilder(context, PlantDatabase.class, "PlantDatabase").build();
        plantDatabase.dataAccessObject().addPlant(plant);
    }
}
