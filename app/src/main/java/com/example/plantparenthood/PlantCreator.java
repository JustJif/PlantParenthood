package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantCreator
{
    private static int uniquePlantID;
    PlantCreator()
    {
        AsyncTask.execute(() ->
        {
            List<Plant> plants = DatabaseHandler.getDatabase().getPlantsFromDB();
            uniquePlantID = plants.size();
        });
    }

    /**
     * This parses a JSON and creates plants from the JSON.
     * @param nonparsedPlants JSONObject of potential plants to be parsed
     * @param applicationContext context for application (needed to fetch default image)
     * @param plantController An object of plant controller
     * @return A list of parsed plants from API
     */
    public ArrayList<Plant> createPlant(JSONObject nonparsedPlants, Context applicationContext, PlantController plantController) {
        ArrayList<Plant> createdPlantObjects = new ArrayList<>();

        JSONArray plantsList = null;
        try
        {
            plantsList = nonparsedPlants.getJSONArray("data");

            for (int i = 0; i < plantsList.length(); i++) {
                JSONObject currentPlant = plantsList.getJSONObject(i);
                //int id = currentPlant.getInt("id");
                int id = uniquePlantID;
                uniquePlantID++;
                String common_name = currentPlant.getString("common_name");
                JSONArray scientific_name = currentPlant.getJSONArray("scientific_name");
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

                String[] sunlightArray = new String[sunlight.length()];
                for (int j = 0; j < sunlight.length(); j++) {
                    sunlightArray[j] = sunlight.getString(j);
                }

                Plant newPlant = new Plant.PlantBuilder()
                        .setId(id)
                        .setCommon_name(common_name)
                        .setScientific_name(plantScientificNames[0])
                        .setCycle(cycle)
                        .setWatering(watering)
                        .setSunlight(sunlightArray[0])
                        .setPlantImageURL(imageURL)
                        .setDefault_image(BitmapFactory.decodeResource(applicationContext.getResources(), R.drawable.defaultimage))
                        .buildPlant();

                createdPlantObjects.add(newPlant);
            }

            if(plantController != null)
                plantController.passPlantslist(createdPlantObjects, nonparsedPlants.getInt("current_page"), nonparsedPlants.getInt("last_page"));
        } catch (JSONException e) {
            return null;
        }
        return createdPlantObjects;
    }

    /**
     * This adds the plant to the database
     */
    public void addPlant(Plant plant)
    {
        AsyncTask.execute(() -> DatabaseHandler.getDatabase().addPlantToDatabase(plant));
    }

    public void addCustomPlant(Context applicationContext, String name, Bitmap image, String sciName) {
        Plant newPlant = new Plant.PlantBuilder()
                .setId(uniquePlantID)
                .setCommon_name(name)
                .setDefault_image(image)
                .setScientific_name(sciName)
                .setDefault_image(BitmapFactory.decodeResource(applicationContext.getResources(), R.drawable.defaultimage))
                .buildPlant();
        AsyncTask.execute(() -> DatabaseHandler.getDatabase(applicationContext).addPlantToDatabase(newPlant));
        uniquePlantID++;
    }

    /**
     * When loading, a plant has to be created from the database, this class handles the
     * creation of the plant
     * @param newPlant the plant within the database
     * @param wateringCycle a possible watering cycle, this may not exist if a schedule doesn't exist for it
     * @return A fully loaded and initialized plant
     */
    public Plant createPlantFromDatabase(PlantSaveToDatabase newPlant, Watering wateringCycle) {
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

        if(wateringCycle != null)
            plantBuilder.setWateringCycle(wateringCycle);

        Plant plant = plantBuilder.buildPlant();

        return plant;
    }
}
