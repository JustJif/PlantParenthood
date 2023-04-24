package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantCreator {
    public DataAccessObject database;

    PlantCreator(DataAccessObject newDatabase) {
        database = newDatabase;
    }

    public void addPlant(JSONObject nonparsedPlants, PlantSearcher makethisplantUI) throws JSONException {
        ArrayList<Plant> createdPlantObjects = new ArrayList<>();
        JSONArray plantsList = nonparsedPlants.getJSONArray("data");

        for (int i = 0; i < plantsList.length(); i++) {
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
            for (int j = 0; j < scientific_name.length(); j++) {
                plantScientificNames[j] = scientific_name.getString(j);
            }

            String[] plantOtherNames = new String[other_name.length()];
            for (int k = 0; k < scientific_name.length(); k++)
            {
                plantOtherNames[k] = other_name.getString(k);
            }

            String[] sunlightArray = new String[sunlight.length()];
            for (int j = 0; j < sunlight.length(); j++) {
                sunlightArray[j] = sunlight.getString(j);
            }

            Plant newPlant = new Plant.PlantBuilder()
                    .setId(id).setCommon_name(common_name)
                    .setScientific_name(plantScientificNames[0])
                    .setOther_name(plantOtherNames[0])
                    .setCycle(cycle)
                    .setWatering(watering)
                    .setSunlight(sunlightArray[0])
                    .setPlantImageURL(default_image.getString("original_url"))
                    .setDefault_image(BitmapFactory.decodeResource(makethisplantUI.getApplicationContext().getResources(), R.drawable.defaultimage))
                    .buildPlant();

            System.out.println(default_image.getString("original_url"));

            createdPlantObjects.add(newPlant);
        }

        Integer currentPage = nonparsedPlants.getInt("current_page");
        Integer lastPage = nonparsedPlants.getInt("last_page");
        makethisplantUI.createPlantGrid(createdPlantObjects, currentPage, lastPage);
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
