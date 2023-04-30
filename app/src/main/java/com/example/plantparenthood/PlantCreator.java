package com.example.plantparenthood;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlantCreator {
    public DataAccessObject database;

    PlantCreator(DataAccessObject newDatabase) {
        database = newDatabase;
    }

    public ArrayList<Plant> createPlant(JSONObject nonparsedPlants, Context applicationContext, PlantSearcher makethisplantUI) {
        ArrayList<Plant> createdPlantObjects = new ArrayList<>();

        JSONArray plantsList = null;
        try
        {
            plantsList = nonparsedPlants.getJSONArray("data");

            for (int i = 0; i < plantsList.length(); i++) {
                JSONObject currentPlant = plantsList.getJSONObject(i);
                int id = currentPlant.getInt("id");
                String common_name = currentPlant.getString("common_name");
                JSONArray scientific_name = currentPlant.getJSONArray("scientific_name");
                JSONArray other_name = currentPlant.getJSONArray("other_name");
                String cycle = currentPlant.getString("cycle");
                String watering = currentPlant.getString("watering");
                JSONArray sunlight = currentPlant.getJSONArray("sunlight");

                JSONObject default_image = null;
                String imageURL = "";
                if(currentPlant.has("default_image") && !currentPlant.isNull("default_image")) {
                    default_image = currentPlant.getJSONObject("default_image");
                    imageURL = default_image.getString("original_url");
                }

                String[] plantScientificNames = new String[scientific_name.length()];
                for (int j = 0; j < scientific_name.length(); j++) {
                    plantScientificNames[j] = scientific_name.getString(j);
                }

                /*String[] plantOtherNames = new String[other_name.length()];
                for (int k = 0; k < scientific_name.length(); k++)
                {
                    plantOtherNames[k] = other_name.getString(k);
                }*/

                String[] sunlightArray = new String[sunlight.length()];
                for (int j = 0; j < sunlight.length(); j++) {
                    sunlightArray[j] = sunlight.getString(j);
                }

                Plant newPlant = new Plant.PlantBuilder()
                        .setId(id)
                        .setCommon_name(common_name)
                        .setScientific_name(plantScientificNames[0])
                        //.setOther_name(plantOtherNames[0])
                        .setCycle(cycle)
                        .setWatering(watering)
                        .setSunlight(sunlightArray[0])
                        .setPlantImageURL(imageURL)
                        .setDefault_image(BitmapFactory.decodeResource(applicationContext.getResources(), R.drawable.defaultimage))
                        .buildPlant();

                createdPlantObjects.add(newPlant);
            }

            Integer currentPage = nonparsedPlants.getInt("current_page");
            Integer lastPage = nonparsedPlants.getInt("last_page");
            if(makethisplantUI != null)
                makethisplantUI.createPlantGrid(createdPlantObjects, currentPage, lastPage);
        } catch (JSONException e) {
            return null;
        }
        return createdPlantObjects;
    }

    public void addCustomPlant() {

    }

    public Plant createPlantFromDatabase(PlantSaveToDatabase newPlant) {
        Plant.PlantBuilder plantBuilder = new Plant.PlantBuilder();
        plantBuilder.setId(newPlant.getId());
        plantBuilder.setCommon_name(newPlant.getCommon_name());
        plantBuilder.setScientific_name(newPlant.getScientific_name());

        if (!TextUtils.isEmpty(newPlant.getCycle()))
            plantBuilder.setCycle(newPlant.getCycle());

        if (!TextUtils.isEmpty(newPlant.getWatering()))
            plantBuilder.setWatering(newPlant.getWatering());

        if (!TextUtils.isEmpty(newPlant.getSunlight()))
            plantBuilder.setSunlight(newPlant.getSunlight());

        if (newPlant.getDefault_image().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(newPlant.getDefault_image(), 0, newPlant.getDefault_image().length);
            plantBuilder.setDefault_image(bitmap);
        }

        if (!TextUtils.isEmpty(newPlant.getPlantImageURL()))
            plantBuilder.setPlantImageURL(newPlant.getPlantImageURL());

        Plant plant = plantBuilder.buildPlant();

        return plant;
    }

    public void updatePlant(Plant oldPlant, ArrayList<String> changes)
    {

    }
}
