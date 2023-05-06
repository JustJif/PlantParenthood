package com.example.plantparenthood;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantController
{
    private PlantCreator plantCreator;
    private Schedule schedule;
    private PlantSearcherActivity plantSearcherActivity;
    private AbstractCreatorAdapter abstractCreator;
    private PlantSearcher plantSearcher;

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

    public boolean updatePlant(Plant plant, boolean[] changes, String[] textBoxes, Bitmap newImage)
    {
        return plant.updatePlant(changes,textBoxes,newImage);
    }

    public void queryImageAPI(Plant plant, int plantLocation)
    {
        plantSearcher.queryImageAPI(plant,plantLocation);
    }

    public void addPlant(Plant plant)
    {
        plantCreator.addPlant(plant);
    }

    public void addPlant(Plant plant)
    {
        plantCreator.addPlant(plant);
    }

    public void passPlantslist(ArrayList<Plant> plantsList,Integer currentPage, Integer lastPage)
    {
        plantSearcherActivity.createPlantGrid(plantsList,currentPage,lastPage);
    }

    public String searchByNameForPlant(String plantName, Integer pageNumber)
    {
        return plantSearcher.searchByNameForPlant(plantName, pageNumber);
    }

    public void cancelQueueRequests()
    {
        plantSearcher.cancelQueueRequests();
    }

    public void notifyAbstractCreator(int position)
    {
        abstractCreator.notifyChange(position);
    }

    public void setPlantCreator(PlantCreator plantCreator) {
        this.plantCreator = plantCreator;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    public void setPlantSearcherActivity(PlantSearcherActivity plantSearcherActivity) {
        this.plantSearcherActivity = plantSearcherActivity;
    }

    public void setPlantSearcher(PlantSearcher plantSearcher) {
        this.plantSearcher = plantSearcher;
    }

    public void setAbstractCreator(AbstractCreatorAdapter abstractCreator) {
        this.abstractCreator = abstractCreator;
    }
}
