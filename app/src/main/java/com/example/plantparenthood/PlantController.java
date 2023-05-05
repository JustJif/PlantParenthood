package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.EditText;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantController
{
    private PlantCreator plantCreator;
    private AbstractAPI abstractAPI;
    private Schedule schedule;
    private PlantSearcherActivity plantSearcherActivity;

    public PlantController() {}

    public void addPlantSchedule(Plant plant, int wateringInterval)
    {
        schedule.addPlantSchedule(plant,wateringInterval);
    }

    public void removePlantSchedule(Watering water)
    {
        schedule.deletePlantSchedule(water);
    }

    public void updatePlantSchedule(Plant thisPlant, int newValue)
    {
        schedule.updatePlantSchedule(thisPlant,newValue);
    }

    public List<Plant> findScheduledPlantsForToday(List<Plant> listOfPlants, int dateOnCalender)
    {
        return schedule.findScheduledPlantsForToday(listOfPlants,dateOnCalender);
    }

    public boolean updatePlant(Plant plant, boolean[] changes, EditText[] textBoxes, Bitmap newImage)
    {
        return plant.updatePlant(changes,textBoxes,newImage);
    }

    public void queryAPI(RequestQueue queue, String queryParams, Integer page)
    {
        abstractAPI.queryAPI(queue,queryParams,page);
    }

    public void queryImageAPI(RequestQueue queue, Plant plant, AbstractCreatorAdapter plantAdapter, int plantLocation)
    {
        abstractAPI.queryImageAPI(queue,plant,plantAdapter,plantLocation);
    }

    public ArrayList<Plant> createPlant(JSONObject nonparsedPlants, Context context)
    {
        return plantCreator.createPlant(nonparsedPlants, context, this);
    }

    public void passPlantslist(ArrayList<Plant> plantsList,Integer currentPage, Integer lastPage)
    {
        plantSearcherActivity.createPlantGrid(plantsList,currentPage,lastPage);
    }


    public void setPlantCreator(PlantCreator plantCreator) {
        this.plantCreator = plantCreator;
    }

    public void setAPI(AbstractAPI abstractAPI) {
        this.abstractAPI = abstractAPI;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    public void setPlantSearcherActivity(PlantSearcherActivity plantSearcherActivity) {
        this.plantSearcherActivity = plantSearcherActivity;
    }
}
