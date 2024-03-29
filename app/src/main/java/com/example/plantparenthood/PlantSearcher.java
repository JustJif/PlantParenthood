package com.example.plantparenthood;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class PlantSearcher
{
    private AbstractAPI api;
    private RequestQueue queue;

    public PlantSearcher(PlantController plantController, Context context)
    {
        api = new Perenual(plantController,context);
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Searched the api by name for a plant, given that the name isnt an empty string
     * @param nameOfPlant the name of the plant
     * @param pageNumber the page to search (each page display 30 plants)
     * @return either a success or a failure string
     */
    public String searchByNameForPlant(String nameOfPlant, Integer pageNumber)
    {
        if (!nameOfPlant.equals(""))
        {
            api.queryAPI(queue, nameOfPlant, pageNumber);
            return "Loading...";
        }
        else
        {
            return "Error no name";
        }
    }

    /**
     * stops spamming the api with queries, really helps with performance
     */
    public void cancelQueueRequests()
    {
        queue.cancelAll(DatabaseHandler.getDatabase());
    }

    /**
     * Queries an image for each plant found by the API,
     * due to the size of images they have to be queried separately than plans
     * @param plant the plant to query the image for
     * @param plantLocation the location of the plant within the recycler
     */
    public void queryImageAPI(Plant plant, int plantLocation)
    {
        api.queryImageAPI(queue,plant,plantLocation);
    }

    public void filterSearchResult(String query, String watering, String cycle, String sunlight)
    {

    }
    /*public List<Plant> filterPlants(List<Plant> plants, boolean fullShade, boolean minimum, boolean average, boolean frequent, boolean perennial, boolean none, boolean fullSun, boolean sunPartShade, boolean partShade, boolean biannual, boolean biennial, boolean annual) {
        List<Plant> filteredPlants = new ArrayList<>();

        for (Plant plant : plants) {
            if ((fullShade && plant.isFullShade()) || (minimum && plant.isMinimum()) || (average && plant.isAverage()) || (frequent && plant.isFrequent()) || (perennial && plant.isPerennial()) || (none && plant.isNone()) || (fullSun && plant.isFullSun()) || (sunPartShade && plant.isSunPartShade()) || (partShade && plant.isPartShade()) || (biannual && plant.isBiannual()) || (biennial && plant.isBiennial()) || (annual && plant.isAnnual())) {
                filteredPlants.add(plant);
            }
        }

        return filteredPlants;
    }*/

}
